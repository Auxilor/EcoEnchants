package com.willfp.ecoenchants.enchantments.util;

import com.google.common.collect.Sets;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.TridentStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

public class WatcherTriggers implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        if(!(((Arrow) event.getDamager()).getShooter() instanceof LivingEntity))
            return;

        LivingEntity attacker = (LivingEntity) ((Arrow) event.getDamager()).getShooter();
        Arrow arrow = (Arrow) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if(attacker instanceof Player) {
            if (!AntigriefManager.canInjure((Player) attacker, victim)) return;
        }

        if(event.isCancelled()) return;

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.arrow(arrow, enchant)) return;
            int level = EnchantChecks.getArrowLevel(arrow, enchant);
            enchant.onArrowDamage(attacker, victim, arrow, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onTridentDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Trident))
            return;

        if(!(((Trident) event.getDamager()).getShooter() instanceof LivingEntity))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (event.isCancelled())
            return;

        LivingEntity attacker = (LivingEntity) ((Trident) event.getDamager()).getShooter();
        Trident trident = (Trident) event.getDamager();
        ItemStack item = TridentStack.getTridentStack(trident);

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(attacker instanceof Player) {
            if (!AntigriefManager.canInjure((Player) attacker, victim)) return;
        }

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.item(item, enchant)) return;
            int level = EnchantChecks.getItemLevel(item, enchant);
            enchant.onTridentDamage(attacker, victim, trident, level, event);
        }));
    }


    private static final Set<UUID> prevPlayersOnGround = Sets.newHashSet();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @EventHandler(ignoreCancelled = true)
    public void onJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getVelocity().getY() > 0) {
            float jumpVelocity = 0.42f;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += ((float) player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
            }
            jumpVelocity = Float.parseFloat(df.format(jumpVelocity).replace(',', '.'));
            if (event.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!player.isOnGround() && Float.compare((float) player.getVelocity().getY(), jumpVelocity) == 0) {
                    EcoEnchants.getAll().forEach((enchant -> {
                        int level = EnchantChecks.getArmorPoints(player, enchant);
                        if(level == 0) return;
                        enchant.onJump(player, level, event);
                    }));
                }
            }
        }
        if (player.isOnGround()) {
            prevPlayersOnGround.add(player.getUniqueId());
        } else {
            prevPlayersOnGround.remove(player.getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMeleeAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (event.isCancelled())
            return;

        LivingEntity attacker = (LivingEntity) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if(attacker instanceof Player) {
            if (!AntigriefManager.canInjure((Player) attacker, victim)) return;
        }

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.mainhand(attacker, enchant)) return;
            int level = EnchantChecks.getMainhandLevel(attacker, enchant);
            enchant.onMeleeAttack(attacker, victim, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event) {
        if (event.getProjectile().getType() != EntityType.ARROW)
            return;

        LivingEntity shooter = event.getEntity();

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.mainhand(shooter, enchant)) return;
            int level = EnchantChecks.getMainhandLevel(shooter, enchant);
            enchant.onBowShoot(shooter, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(EntityDamageEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            return;

        if(!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        EcoEnchants.getAll().forEach((enchant -> {
            int level = EnchantChecks.getArmorPoints(victim, enchant);
            if(level == 0) return;
            enchant.onFallDamage(victim, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onArrowHit(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof LivingEntity))
            return;

        if (!(event.getEntity() instanceof Arrow)) return;

        Arrow arrow = (Arrow) event.getEntity();
        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.arrow(arrow, enchant)) return;
            int level = EnchantChecks.getArrowLevel(arrow, enchant);
            enchant.onArrowHit(shooter, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onTridentHit(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof LivingEntity))
            return;

        if (!(event.getEntity() instanceof Trident)) return;

        Trident trident = (Trident) event.getEntity();
        ItemStack item = TridentStack.getTridentStack(trident);
        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.item(item, enchant)) return;
            int level = EnchantChecks.getItemLevel(item, enchant);
            enchant.onTridentHit(shooter, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(!AntigriefManager.canBreakBlock(player, block)) return;

        if (event.isCancelled())
            return;

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.mainhand(player, enchant)) return;
            int level = EnchantChecks.getMainhandLevel(player, enchant);
            enchant.onBlockBreak(player, block, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageWearingArmor(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        EcoEnchants.getAll().forEach((enchant -> {
            int level = EnchantChecks.getArmorPoints(victim, enchant);
            if(level == 0) return;
            enchant.onDamageWearingArmor(victim, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            EcoEnchants.getAll().forEach((enchant -> {
                int level = EnchantChecks.getArmorPoints(player, enchant);
                enchant.onArmorEquip(player, level, event);
            }));
        }, 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageBlock(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.mainhand(player, enchant)) return;
            int level = EnchantChecks.getMainhandLevel(player, enchant);
            enchant.onDamageBlock(player, block, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onTridentLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity() instanceof Trident))
            return;

        if(!(event.getEntity().getShooter() instanceof LivingEntity))
            return;

        Trident trident = (Trident) event.getEntity();
        LivingEntity shooter = (LivingEntity) trident.getShooter();
        ItemStack item = TridentStack.getTridentStack(trident);

        EcoEnchants.getAll().forEach((enchant -> {
            if (!EnchantChecks.item(item, enchant)) return;
            int level = EnchantChecks.getItemLevel(item, enchant);
            enchant.onTridentLaunch(shooter, trident, level, event);
        }));
    }
}
