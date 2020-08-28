package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.AntiGrief;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class Flinch extends EcoEnchant {
    public Flinch() {
        super(
                new EcoEnchantBuilder("flinch", EnchantmentType.NORMAL, Target.Applicable.SHIELD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onFlinch(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof LivingEntity))
            return;

        Player player = (Player) event.getEntity();

        LivingEntity victim = (LivingEntity) event.getDamager();

        if(!player.isBlocking()) return;

        if(victim instanceof Player) {
            if(!AntiGrief.canInjurePlayer(player, (Player) event.getEntity())) return;
        }

        int level;
        if (!HasEnchant.playerOffhand(player, this) && !HasEnchant.playerHeld(player, this)) return;
        if(HasEnchant.playerOffhand(player, this)) level = HasEnchant.getPlayerOffhandLevel(player, this);
        else level = HasEnchant.getPlayerLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");

        double finalChance = (chance * level)/100;
        if(Rand.randFloat(0, 1) > finalChance) return;

        int finalDuration = duration * level;

        victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, finalDuration, 1, false, false, false));
    }
}
