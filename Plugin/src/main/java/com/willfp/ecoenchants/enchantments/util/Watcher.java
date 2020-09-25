package com.willfp.ecoenchants.enchantments.util;

import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;

public interface Watcher {
    default void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {}
    default void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {}
    default void onJump(Player player, int level, PlayerMoveEvent event) {}
    default void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {}
    default void onBowShoot(LivingEntity shooter, Arrow arrow, int level, EntityShootBowEvent event) {}
    default void onFallDamage(LivingEntity faller, int level, EntityDamageEvent event) {}
    default void onArrowHit(LivingEntity shooter, int level, ProjectileHitEvent event) {}
    default void onTridentHit(LivingEntity shooter, int level, ProjectileHitEvent event) {}
    default void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {}
    default void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {}
    default void onArmorEquip(Player player, int level, ArmorEquipEvent event) {}
    default void onDamageBlock(Player player, Block block, int level, BlockDamageEvent event) {}
    default void onTridentLaunch(LivingEntity shooter, Trident trident, int level, ProjectileLaunchEvent event) {}
    default void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {}
}
