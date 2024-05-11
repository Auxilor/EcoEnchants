package com.willfp.ecoenchants.proxy.v1_20_6

import com.willfp.eco.core.Prerequisite
import com.willfp.ecoenchants.CodecReplacerProxy
import com.willfp.ecoenchants.proxy.v1_20_6.registration.VanillaEcoEnchantsEnchantment
import com.willfp.ecoenchants.setStaticFinal
import net.minecraft.core.Holder
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import org.bukkit.craftbukkit.util.CraftMagicNumbers

class CodecReplacer : CodecReplacerProxy {
    override fun replaceItemCodec() {
        ItemStack::class.java
            .getDeclaredField("OPTIONAL_STREAM_CODEC")
            .setStaticFinal(PatchedStreamCodec)

        ItemStack::class.java
            .getDeclaredField("OPTIONAL_LIST_STREAM_CODEC")
            .setStaticFinal(
                PatchedStreamCodec.apply(ByteBufCodecs.collection { size ->
                    NonNullList.createWithCapacity(size)
                })
            )

        ServerboundSetCreativeModeSlotPacket.STREAM_CODEC::class.java
            .getDeclaredField("val\$codec2")
            .apply {
                isAccessible = true
                set(
                    ServerboundSetCreativeModeSlotPacket.STREAM_CODEC,
                    ItemStack.validatedStreamCodec(PatchedStreamCodec)
                )
            }
    }

    private object PatchedStreamCodec : StreamCodec<RegistryFriendlyByteBuf, ItemStack> {
        private const val nbtKey = "ecoenchants:client_enchs_patch"
        private const val nbtKeyShow = "ecoenchants:client_enchs_patch_show"
        private const val nbtKeyGlint = "ecoenchants:client_enchs_patch_glint"

        private val itemStreamCodec = ByteBufCodecs.holderRegistry(Registries.ITEM)

        fun Enchantment.isEco() = this is VanillaEcoEnchantsEnchantment

        fun Map<Enchantment, Int>.toComponent(): ItemEnchantments {
            val component = ItemEnchantments.Mutable(ItemEnchantments.EMPTY)
            for ((enchant, level) in this) {
                component.set(enchant, level)
            }
            return component.toImmutable()
        }

        fun serialize(itemStack: ItemStack): ItemStack {
            val enchantments = itemStack.get(DataComponents.ENCHANTMENTS) ?: return itemStack

            // Separate out enchantments into vanilla and custom
            val vanillaEnchantments = mutableMapOf<Enchantment, Int>()
            val customEnchantments = mutableMapOf<Enchantment, Int>()

            for ((holder, level) in enchantments.entrySet()) {
                val enchant = holder.value()
                if (enchant.isEco()) {
                    customEnchantments[enchant] = level
                } else {
                    vanillaEnchantments[enchant] = level
                }
            }

            if (customEnchantments.isEmpty()) {
                return itemStack
            }

            // Only keep vanilla enchants in NBT
            itemStack.set(
                DataComponents.ENCHANTMENTS,
                vanillaEnchantments
                    .toComponent()
                    .withTooltip(enchantments.showInTooltip)
            )

            // Override glint
            val hasGlint = itemStack.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE)
            itemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)

            // Put custom enchants in custom data
            val customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).update {
                it.remove(nbtKey)

                val tag = ListTag()

                for ((enchant, level) in customEnchantments) {
                    val id = BuiltInRegistries.ENCHANTMENT.getKey(enchant) ?: continue

                    tag.add(CompoundTag().apply {
                        putString("id", id.toString())
                        putInt("lvl", level)
                    })
                }

                it.put(nbtKey, tag)
                it.putBoolean(nbtKeyShow, enchantments.showInTooltip)
                if (hasGlint != null) {
                    it.putBoolean(nbtKeyGlint, hasGlint)
                }
            }

            itemStack.set(DataComponents.CUSTOM_DATA, customData)

            return itemStack
        }

        fun deserialize(itemStack: ItemStack): ItemStack {
            val customData = itemStack.get(DataComponents.CUSTOM_DATA) ?: return itemStack

            // Fetch enchantments from custom data
            val enchantments = ItemEnchantments.Mutable(
                itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
            )

            val customDataTag = customData.copyTag()

            customDataTag.getList(nbtKey, CraftMagicNumbers.NBT.TAG_COMPOUND)
                .filterIsInstance<CompoundTag>()
                .forEach { tag ->
                    val id = tag.getString("id")
                    val level = tag.getInt("lvl")

                    val enchant = BuiltInRegistries.ENCHANTMENT.get(
                        ResourceLocation(id)
                    ) ?: return@forEach

                    enchantments.set(enchant, level)
                }

            if (enchantments.keySet().isEmpty()) {
                return itemStack
            }

            enchantments.showInTooltip = customDataTag.getBoolean(nbtKeyShow)

            // Remove extra data
            val cleaned = customData.update {
                it.remove(nbtKey)
                it.remove(nbtKeyShow)
                it.remove(nbtKeyGlint)
            }

            // Set to item
            itemStack.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable())

            // Reset glint
            val hasGlint = if (customDataTag.contains(nbtKeyGlint)) {
                customDataTag.getBoolean(nbtKeyGlint)
            } else {
                null
            }

            itemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, hasGlint)

            // Set cleaned data
            if (cleaned.isEmpty) {
                itemStack.remove(DataComponents.CUSTOM_DATA)
            } else {
                itemStack.set(DataComponents.CUSTOM_DATA, cleaned)
            }

            return itemStack
        }

        override fun decode(buf: RegistryFriendlyByteBuf): ItemStack {
            val i = buf.readVarInt()
            return if (i <= 0) {
                ItemStack.EMPTY
            } else {
                val holder: Holder<Item> =
                    itemStreamCodec.decode(buf) as Holder<Item>
                val dataComponentPatch =
                    DataComponentPatch.STREAM_CODEC.decode(buf) as DataComponentPatch

                deserialize(ItemStack(holder, i, dataComponentPatch))
            }
        }

        override fun encode(buf: RegistryFriendlyByteBuf, itemStack: ItemStack) {
            @Suppress("SENSELESS_COMPARISON")
            if (itemStack.isEmpty || itemStack.item == null) {
                buf.writeVarInt(0)
            } else {
                val remapped = serialize(itemStack)

                buf.writeVarInt(remapped.count)

                itemStreamCodec.encode(
                    buf,
                    remapped.itemHolder
                )

                if (Prerequisite.HAS_PAPER.isMet) {
                    // Paper start - adventure; conditionally render translatable components
                    val prev = ComponentSerialization.DONT_RENDER_TRANSLATABLES.get()
                    try {
                        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(true)
                        DataComponentPatch.STREAM_CODEC.encode(buf, remapped.componentsPatch)
                    } finally {
                        ComponentSerialization.DONT_RENDER_TRANSLATABLES.set(prev)
                    }
                    // Paper end - adventure; conditionally render translatable components
                }
            }
        }
    }
}
