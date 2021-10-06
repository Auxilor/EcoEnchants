package com.willfp.ecoenchants.alchemy;


import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class Alchemy extends EcoEnchant {
    /**
     * true Metadata key.
     */
    private final FixedMetadataValue metaKeyTrue = this.getPlugin().getMetadataValueFactory().create(true);

    /**
     * Instantiate Alchemy Enchantment.
     */
    public Alchemy() {
        super("alchemy", EnchantmentType.NORMAL);
    }

    /**
     * Enchantment functionality.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onPotionEffect(@NotNull final EntityPotionEffectEvent event) {
        if (event.getNewEffect() == null) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        if (!(event.getCause() == Cause.POTION_DRINK) && !(event.getCause() == Cause.POTION_SPLASH) ) {
            return;
        }

        if (entity.hasMetadata(event.getNewEffect().toString())) {
            return;
        }

        int level = EnchantChecks.getArmorPoints(entity, this);
        if (level == 0) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }
        if (this.getDisabledWorlds().contains(entity.getWorld())) {
            return;
        }

        PotionEffect effect = event.getNewEffect();

        PotionEffect newEffect = new PotionEffect(
                effect.getType(),
                effect.getDuration(),
                ((effect.getAmplifier() + 1) * 2) - 1,
                effect.isAmbient(),
                effect.hasParticles(),
                effect.hasIcon()
        );

        entity.setMetadata(newEffect.toString(), metaKeyTrue);

        entity.removePotionEffect(effect.getType());

        this.getPlugin().getScheduler().run(() -> newEffect.apply(entity));

        this.getPlugin().getScheduler().runLater(() -> entity.removeMetadata(newEffect.toString(), this.getPlugin()), 1);
    }
}
