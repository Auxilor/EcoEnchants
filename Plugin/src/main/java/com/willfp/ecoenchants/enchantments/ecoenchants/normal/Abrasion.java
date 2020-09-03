package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
public class Abrasion extends EcoEnchant {
    public Abrasion() {
        super(
                new EcoEnchantBuilder("abrasion", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        if (event.isCancelled())
            return;

        Player player = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if(!AntigriefManager.canInjure(player, victim)) return;

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        boolean notcharged = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged");
        if (Cooldown.getCooldown(player) != 1.0f && !notcharged)
            return;

        ArrayList<ItemStack> armor = new ArrayList<ItemStack>(Arrays.asList(victim.getInventory().getArmorContents()));
        if (armor.isEmpty())
            return;

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null)
                continue;

            if (Target.Applicable.HELMET.getMaterials().contains(armorPiece.getType())) {
                ItemDurability.damageItem(player, player.getInventory().getHelmet(), level, 39);
            }
            if (Target.Applicable.CHESTPLATE.getMaterials().contains(armorPiece.getType())) {
                ItemDurability.damageItem(player, player.getInventory().getChestplate(), level, 38);
            }
            if (Target.Applicable.LEGGINGS.getMaterials().contains(armorPiece.getType())) {
                ItemDurability.damageItem(player, player.getInventory().getLeggings(), level, 37);
            }
            if (Target.Applicable.BOOTS.getMaterials().contains(armorPiece.getType())) {
                ItemDurability.damageItem(player, player.getInventory().getBoots(), level, 36);
            }
        }
    }
}
