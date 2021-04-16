package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boss;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class BossHunter extends EcoEnchant {
    public BossHunter() {
        super(
                "boss_hunter", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(victim instanceof Boss || victim instanceof ElderGuardian) && !victim.getPersistentDataContainer().has(new NamespacedKey("ecobosses", "boss"), PersistentDataType.STRING)) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        double damageMultiplier = (level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}
