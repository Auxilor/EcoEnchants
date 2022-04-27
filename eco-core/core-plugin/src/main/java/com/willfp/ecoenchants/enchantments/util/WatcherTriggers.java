package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.events.ArmorChangeEvent;
import com.willfp.eco.core.events.ArmorEquipEvent;
import com.willfp.eco.core.events.PlayerJumpEvent;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import com.willfp.eco.util.PlayerUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.libreforge.LibReforgeUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WatcherTriggers extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * If watchers should be triggered against npcs.
     */
    private static boolean allowOnNPC = false;

    /**
     * Create new listener for watcher events.
     *
     * @param plugin The plugin to link the events to.
     */
    public WatcherTriggers(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Update if allowed on npc.
     *
     * @param plugin Instance of EcoEnchants.
     */
    @ConfigUpdater
    public static void update(@NotNull final EcoEnchantsPlugin plugin) {
        allowOnNPC = plugin.getConfigYml().getBool("allow-on-npc");
    }

    /**
     * Called when an entity shoots another entity with an arrow.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onArrowDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Arrow arrow)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (((Arrow) event.getDamager()).getShooter() == null) {
            return;
        }

        if (!(((Arrow) event.getDamager()).getShooter() instanceof LivingEntity attacker)) {
            return;
        }

        if (!allowOnNPC) {
            if (victim.hasMetadata("NPC")) {
                return;
            }
        }

        if (attacker instanceof Player && !AntigriefManager.canInjure((Player) attacker, victim)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        EnchantChecks.getEnchantsOnArrow(arrow).forEach(((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(attacker)) {
                return;
            }

            enchant.onArrowDamage(attacker, victim, arrow, level, event);
        }));
    }

    /**
     * Called when an entity damages another entity with a trident throw.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Trident trident)) {
            return;
        }

        if (!(((Trident) event.getDamager()).getShooter() instanceof LivingEntity attacker)) {
            return;
        }

        if (((Trident) event.getDamager()).getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        ItemStack item = trident.getItem();

        if (!allowOnNPC) {
            if (victim.hasMetadata("NPC")) {
                return;
            }
        }

        if (attacker instanceof Player && !AntigriefManager.canInjure((Player) attacker, victim)) {
            return;
        }

        EnchantChecks.getEnchantsOnItem(item).forEach(((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(attacker)) {
                return;
            }

            enchant.onTridentDamage(attacker, victim, trident, level, event);
        }));
    }

    /**
     * Called when a player jumps.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onJump(@NotNull final PlayerJumpEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();

        EnchantChecks.getEnchantsOnArmor(player).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(player)) {
                return;
            }

            enchant.onJump(player, level, event);
        });
    }

    /**
     * Called when an entity attacks another entity with a melee attack.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onMeleeAttack(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof LivingEntity attacker)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.THORNS) {
            return;
        }

        if (!allowOnNPC) {
            if (victim.hasMetadata("NPC")) {
                return;
            }
        }

        if (attacker instanceof Player && !AntigriefManager.canInjure((Player) attacker, victim)) {
            return;
        }

        EnchantChecks.getEnchantsOnMainhand(attacker).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(attacker)) {
                return;
            }

            enchant.onMeleeAttack(attacker, victim, level, event);
        });
    }

    /**
     * Called when an entity shoots a bow.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(@NotNull final EntityShootBowEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (event.getProjectile().getType() != EntityType.ARROW) {
            return;
        }

        LivingEntity shooter = event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();

        EnchantChecks.getEnchantsOnMainhand(shooter).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(shooter)) {
                return;
            }

            enchant.onBowShoot(shooter, arrow, level, event);
        });
    }

    /**
     * Called when an entity launches a projectile.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(@NotNull final ProjectileLaunchEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof AbstractArrow)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        Projectile projectile = event.getEntity();

        if (shooter.getEquipment() == null) {
            return;
        }

        ItemStack item = shooter.getEquipment().getItemInMainHand();

        if (projectile instanceof Trident trident) {
            item = trident.getItem();
        }

        EnchantChecks.getEnchantsOnItem(item).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(shooter)) {
                return;
            }

            enchant.onProjectileLaunch(shooter, projectile, level, event);
        });
    }

    /**
     * Called when an entity takes fall damage.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(@NotNull final EntityDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        EnchantChecks.getEnchantsOnArmor(victim).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(victim)) {
                return;
            }

            enchant.onFallDamage(victim, level, event);
        });
    }

    /**
     * Called when an arrow hits a block or entity.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onArrowHit(@NotNull final ProjectileHitEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof LivingEntity shooter)) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow arrow)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        EnchantChecks.getEnchantsOnArrow(arrow).forEach(((enchant, level) -> {
            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(shooter)) {
                return;
            }

            enchant.onArrowHit(shooter, level, event);
        }));
    }

    /**
     * Called when a trident hits a block or entity.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentHit(@NotNull final ProjectileHitEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof LivingEntity shooter)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }

        ItemStack item = trident.getItem();

        EnchantChecks.getEnchantsOnItem(item).forEach((enchant, level) -> {
            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(shooter)) {
                return;
            }

            enchant.onTridentHit(shooter, level, event);
        });
    }

    /**
     * Called when a player breaks a block.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(@NotNull final BlockBreakEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        EnchantChecks.getEnchantsOnMainhand(player).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(player)) {
                return;
            }

            enchant.onBlockBreak(player, block, level, event);
        });
    }

    /**
     * Called when an entity takes damage wearing armor.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamageWearingArmor(@NotNull final EntityDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        EnchantChecks.getEnchantsOnArmor(victim).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(victim)) {
                return;
            }

            enchant.onDamageWearingArmor(victim, level, event);
        });
    }

    /**
     * Called when an entity puts on or takes off armor with an enchantment.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    @Deprecated
    public void onArmorEquip(@NotNull final ArmorEquipEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();

        this.getPlugin().getScheduler().runLater(() -> EcoEnchants.values().forEach(enchant -> {
            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(player)) {
                return;
            }

            int level = EnchantChecks.getArmorPoints(player, enchant);
            enchant.onArmorEquip(player, level, event);
        }), 1);
    }

    /**
     * Called when an entity puts on or takes off armor with an enchantment.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onArmorChange(@NotNull final ArmorChangeEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();

        EcoEnchants.values().forEach(enchant -> {
            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(player)) {
                return;
            }

            int level = EnchantChecks.getArmorPoints(player, enchant);
            enchant.onArmorEquip(player, level, event);
        });
    }

    /**
     * Called when a player damages a block.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamageBlock(@NotNull final BlockDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (event.getBlock().getDrops(player.getInventory().getItemInMainHand()).isEmpty()) {
            return;
        }

        EnchantChecks.getEnchantsOnMainhand(player).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(player)) {
                return;
            }

            enchant.onDamageBlock(player, block, level, event);
        });
    }

    /**
     * Called when an entity throws a trident.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentLaunch(@NotNull final ProjectileLaunchEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }

        LivingEntity shooter = (LivingEntity) trident.getShooter();
        ItemStack item = trident.getItem();
        assert shooter != null;

        EnchantChecks.getEnchantsOnItem(item).forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(shooter)) {
                return;
            }

            enchant.onTridentLaunch(shooter, trident, level, event);
        });
    }

    /**
     * Called when a player blocks an attack with a shield.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDeflect(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Player blocker)) {
            return;
        }

        LivingEntity attacker = PlayerUtils.tryAsPlayer(event.getDamager());

        if (attacker == null) {
            return;
        }

        if (!blocker.isBlocking()) {
            return;
        }

        if (!AntigriefManager.canInjure(blocker, attacker)) {
            return;
        }

        Map<EcoEnchant, Integer> enchants = blocker.getInventory().getItemInMainHand().getType() == Material.SHIELD
                ? EnchantChecks.getEnchantsOnMainhand(blocker)
                : EnchantChecks.getEnchantsOnOffhand(blocker);

        enchants.forEach((enchant, level) -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(blocker.getWorld())) {
                return;
            }

            if (!enchant.areRequirementsMet(blocker)) {
                return;
            }

            enchant.onDeflect(blocker, attacker, level, event);
        });
    }
}
