package com.willfp.ecoenchants.v1_15_R1;

import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.Enchantment;
import net.minecraft.server.v1_15_R1.EnchantmentSlotType;
import net.minecraft.server.v1_15_R1.EnumChatFormat;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;

public class NMSEnchant extends Enchantment {
    private org.bukkit.enchantments.Enchantment enchantment;

    public NMSEnchant(org.bukkit.enchantments.Enchantment enchantment) {
        super(Enchantment.Rarity.COMMON, EnchantmentSlotType.BREAKABLE, new EnumItemSlot[]{});
        this.enchantment = enchantment;
    }

    public void update(org.bukkit.enchantments.Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public int a(int var0) {
        return Short.MAX_VALUE;
    }

    @Override
    public int getMaxLevel() {
        return enchantment.getMaxLevel();
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean c() {
        return false;
    }

    @Override
    public IChatBaseComponent d(int level) {
        IChatBaseComponent var1 = new ChatMessage(this.g(), new Object[0]);
        if(this.c()) {
            var1.a(EnumChatFormat.RED);
        } else {
            var1.a(EnumChatFormat.GRAY);
        }

        if(level != 1 || this.getMaxLevel() != 1) {
            var1.a(" ").addSibling(new ChatMessage("enchantment.level." + level, new Object[0]));
        }

        return var1;
    }
}
