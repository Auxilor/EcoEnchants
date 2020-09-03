package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Confusion extends EcoEnchant {
    public Confusion() {
        super(
                new EcoEnchantBuilder("confusion", EnchantmentType.SPECIAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void confusionHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getDamager();

        Player victim = (Player) event.getEntity();

        if(!AntigriefManager.canInjure(player, victim)) return;

        if (!EnchantChecks.mainhand(player, this)) return;

        if (Cooldown.getCooldown(player) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        if (NumberUtils.randFloat(0, 1) > level * 0.01 * chance)
            return;

        List<ItemStack> hotbar = new ArrayList<ItemStack>();
        for (int i = 0; i < 9; i++) {
            hotbar.add(victim.getInventory().getItem(i));
        }
        Collections.shuffle(hotbar);
        int i2 = 0;
        for (ItemStack item : hotbar) {
            victim.getInventory().setItem(i2, item);
            i2++;
        }
    }
}
