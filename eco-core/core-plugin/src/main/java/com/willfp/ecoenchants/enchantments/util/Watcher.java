package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.core.events.ArmorChangeEvent;
import com.willfp.eco.core.events.ArmorEquipEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public interface Watcher {

    /**
     * Called when an entity shoots another entity with an arrow.
     *
     * @param attacker The shooter.
     * @param victim   The victim.
     * @param arrow    The arrow entity.
     * @param level    The level of the enchantment on the arrow.
     * @param event    The event that called this watcher.
     */
    default void onArrowDamage(@NotNull final LivingEntity attacker,
                               @NotNull final LivingEntity victim,
                               @NotNull final Arrow arrow,
                               final int level,
                               @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity damages another entity with a trident throw.
     *
     * @param attacker The shooter.
     * @param victim   The victim.
     * @param trident  The trident entity.
     * @param level    The level of the enchantment on the trident.
     * @param event    The event that called this watcher.
     */
    default void onTridentDamage(@NotNull final LivingEntity attacker,
                                 @NotNull final LivingEntity victim,
                                 @NotNull final Trident trident,
                                 final int level,
                                 @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when a player jumps.
     *
     * @param player The player.
     * @param level  The level of the enchantment found on the player's armor.
     * @param event  The event that called this watcher.
     */
    default void onJump(@NotNull final Player player,
                        final int level,
                        @NotNull final PlayerMoveEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity attacks another entity with a melee attack.
     *
     * @param attacker The attacker.
     * @param victim   The victim.
     * @param level    The level of the enchantment found on the attacker's weapon.
     * @param event    The event that called this watcher.
     */
    default void onMeleeAttack(@NotNull final LivingEntity attacker,
                               @NotNull final LivingEntity victim,
                               final int level,
                               @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity shoots a bow.
     *
     * @param shooter The entity that shot the bow.
     * @param arrow   The arrow that was shot.
     * @param level   The level of the enchantment found on the bow.
     * @param event   The event that called this watcher.
     */
    default void onBowShoot(@NotNull final LivingEntity shooter,
                            @NotNull final Arrow arrow,
                            final int level,
                            @NotNull final EntityShootBowEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity shoots a projectile.
     *
     * @param shooter    The entity that shot the bow.
     * @param projectile The projectile that was shot.
     * @param level      The level of the enchantment found on the projectile.
     * @param event      The event that called this watcher.
     */
    default void onProjectileLaunch(@NotNull final LivingEntity shooter,
                                    @NotNull final Projectile projectile,
                                    final int level,
                                    @NotNull final ProjectileLaunchEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity takes fall damage.
     *
     * @param faller The entity that took the fall damage.
     * @param level  The level of the enchantment found on the entity's armor.
     * @param event  The event that called this watcher.
     */
    default void onFallDamage(@NotNull final LivingEntity faller,
                              final int level,
                              @NotNull final EntityDamageEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an arrow hits a block or entity.
     *
     * @param shooter The entity that shot the arrow.
     * @param level   The level of the enchantment found on the arrow.
     * @param event   The event that called this watcher.
     */
    default void onArrowHit(@NotNull final LivingEntity shooter,
                            final int level,
                            @NotNull final ProjectileHitEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when a trident hits a block or entity.
     *
     * @param shooter The entity that threw the trident.
     * @param level   The level of the enchantment found on the trident.
     * @param event   The event that called this watcher.
     */
    default void onTridentHit(@NotNull final LivingEntity shooter,
                              final int level,
                              @NotNull final ProjectileHitEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when a player breaks a block.
     *
     * @param player The player.
     * @param block  The block that was broken.
     * @param level  The level of the enchantment found on the player's main hand item.
     * @param event  The event that called this watcher.
     */
    default void onBlockBreak(@NotNull final Player player,
                              @NotNull final Block block,
                              final int level,
                              @NotNull final BlockBreakEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity takes damage wearing armor.
     *
     * @param victim The entity that took damage.
     * @param level  The level of the enchantment found on the entity's armor.
     * @param event  The event that called this watcher.
     */
    default void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                      final int level,
                                      @NotNull final EntityDamageEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity puts on or takes off armor with an enchantment.
     *
     * @param player The player that equipped the armor.
     * @param level  The level of the enchantment found on the player's armor.
     * @param event  The event that called this watcher.
     */
    @Deprecated
    default void onArmorEquip(@NotNull final Player player,
                              final int level,
                              @NotNull final ArmorEquipEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity puts on or takes off armor with an enchantment.
     *
     * @param player The player that equipped the armor.
     * @param level  The level of the enchantment found on the player's armor.
     * @param event  The event that called this watcher.
     */
    default void onArmorEquip(@NotNull final Player player,
                              final int level,
                              @NotNull final ArmorChangeEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when a player damages a block.
     *
     * @param player The player that damaged the block.
     * @param block  The damaged block.
     * @param level  The level of the enchantment found on the player's main hand.
     * @param event  The event that called this watcher.
     */
    default void onDamageBlock(@NotNull final Player player,
                               @NotNull final Block block,
                               final int level,
                               @NotNull final BlockDamageEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when an entity throws a trident.
     *
     * @param shooter The entity that threw the trident.
     * @param trident The trident that was thrown.
     * @param level   The level of the enchantment found on the trident.
     * @param event   The event that called this watcher.
     */
    default void onTridentLaunch(@NotNull final LivingEntity shooter,
                                 @NotNull final Trident trident,
                                 final int level,
                                 @NotNull final ProjectileLaunchEvent event) {
        // Empty default as enchantments only override required watchers.
    }

    /**
     * Called when a player blocks an attack with a shield.
     *
     * @param blocker  The player that blocked the attack.
     * @param attacker The attacker.
     * @param level    The level of the enchantment found on the shield.
     * @param event    The event that called this watcher.
     */
    default void onDeflect(@NotNull final Player blocker,
                           @NotNull final LivingEntity attacker,
                           final int level,
                           @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as enchantments only override required watchers.
    }
}
