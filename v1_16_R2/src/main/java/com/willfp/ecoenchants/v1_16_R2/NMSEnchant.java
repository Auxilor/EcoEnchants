package com.willfp.ecoenchants.v1_16_R2;

import net.minecraft.server.v1_16_R2.*;

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
    public boolean h() {
        return false;
    }

    @Override
    public boolean i() {
        return false;
    }

    @Override
    public IChatBaseComponent d(int level) {
        IChatMutableComponent var1 = new ChatMessage(enchantment.getName());
        var1.a(EnumChatFormat.WHITE);

        if (level != 1 || this.getMaxLevel() != 1) {
            var1.c(" ").addSibling(new ChatMessage("enchantment.level." + level));
        }

        return var1;
    }
}
