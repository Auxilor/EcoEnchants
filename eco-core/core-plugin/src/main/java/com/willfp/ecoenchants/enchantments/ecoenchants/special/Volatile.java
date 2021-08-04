package com.willfp.ecoenchants.enchantments.ecoenchants.special;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Volatile extends EcoEnchant {
    public Volatile() {
        super(
                "volatile", EnchantmentType.SPECIAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity uncastAttacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(uncastAttacker instanceof Player attacker)) {
            return;
        }

        if (!EnchantmentUtils.isFullyChargeIfRequired(this, attacker)) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        boolean fire = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire");
        boolean breakblocks = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "break-blocks");

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canCreateExplosion(attacker, event.getEntity().getLocation())) {
            return;
        }

        if (breakblocks && !AntigriefManager.canBreakBlock(attacker, event.getEntity().getLocation().getWorld().getBlockAt(event.getEntity().getLocation()))) {
            return;
        }

        Location explosionLoc = victim.getEyeLocation();

        victim.getWorld().createExplosion(explosionLoc, power, fire, breakblocks);
    }
}
