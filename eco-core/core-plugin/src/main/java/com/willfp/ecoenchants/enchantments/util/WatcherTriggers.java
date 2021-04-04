package com.willfp.ecoenchants.enchantments.util;

import com.google.common.collect.Sets;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.eco.core.events.ArmorEquipEvent;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import com.willfp.eco.util.TridentUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class WatcherTriggers extends PluginDependent implements Listener {
    /**
     * For jump listeners.
     */
    private static final Set<UUID> PREVIOUS_PLAYERS_ON_GROUND = Sets.newHashSet();

    /**
     * For jump listeners.
     */
    private static final DecimalFormat FORMAT = new DecimalFormat("0.00");

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
     */
    @ConfigUpdater
    public static void update() {
        allowOnNPC = EcoEnchantsPlugin.getInstance().getConfig().getBoolean("allow-on-npc");
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

        if (!(event.getDamager() instanceof Arrow)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (((Arrow) event.getDamager()).getShooter() == null) {
            return;
        }

        if (!(((Arrow) event.getDamager()).getShooter() instanceof LivingEntity)) {
            return;
        }

        LivingEntity attacker = (LivingEntity) ((Arrow) event.getDamager()).getShooter();
        Arrow arrow = (Arrow) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

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

        if (!(event.getDamager() instanceof Trident)) {
            return;
        }

        if (!(((Trident) event.getDamager()).getShooter() instanceof LivingEntity)) {
            return;
        }

        if (((Trident) event.getDamager()).getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        LivingEntity attacker = (LivingEntity) ((Trident) event.getDamager()).getShooter();
        Trident trident = (Trident) event.getDamager();
        ItemStack item = TridentUtils.getItemStack(trident);

        LivingEntity victim = (LivingEntity) event.getEntity();

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

            enchant.onTridentDamage(attacker, victim, trident, level, event);
        }));
    }

    /**
     * Called when a player jumps.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onJump(@NotNull final PlayerMoveEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getVelocity().getY() > 0) {
            float jumpVelocity = 0.42f;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += ((float) player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
            }
            jumpVelocity = Float.parseFloat(FORMAT.format(jumpVelocity).replace(',', '.'));
            if (event.getPlayer().getLocation().getBlock().getType() != Material.LADDER
                    && PREVIOUS_PLAYERS_ON_GROUND.contains(player.getUniqueId())
                    && !player.isOnGround()
                    && Float.compare((float) player.getVelocity().getY(), jumpVelocity) == 0) {
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

                    enchant.onJump(player, level, event);
                });
            }
        }
        if (player.isOnGround()) {
            PREVIOUS_PLAYERS_ON_GROUND.add(player.getUniqueId());
        } else {
            PREVIOUS_PLAYERS_ON_GROUND.remove(player.getUniqueId());
        }
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

        if (!(event.getDamager() instanceof LivingEntity)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.THORNS) {
            return;
        }

        LivingEntity attacker = (LivingEntity) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

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

        if (projectile instanceof Trident) {
            item = TridentUtils.getItemStack((Trident) projectile);
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

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity victim = (LivingEntity) event.getEntity();

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

        if (!(event.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        Arrow arrow = (Arrow) event.getEntity();
        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        EnchantChecks.getEnchantsOnArrow(arrow).forEach(((enchant, level) -> {
            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) {
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

        if (!(event.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof Trident)) {
            return;
        }

        Trident trident = (Trident) event.getEntity();
        ItemStack item = TridentUtils.getItemStack(trident);
        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        EnchantChecks.getEnchantsOnItem(item).forEach((enchant, level) -> {
            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) {
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

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity victim = (LivingEntity) event.getEntity();

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

            enchant.onDamageWearingArmor(victim, level, event);
        });
    }

    /**
     * Called when an entity puts on or takes off armor with an enchantment.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
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

            int level = EnchantChecks.getArmorPoints(player, enchant);
            enchant.onArmorEquip(player, level, event);
        }), 1);
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

        if (!(event.getEntity() instanceof Trident)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof LivingEntity)) {
            return;
        }

        Trident trident = (Trident) event.getEntity();
        LivingEntity shooter = (LivingEntity) trident.getShooter();
        ItemStack item = TridentUtils.getItemStack(trident);

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

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof LivingEntity)) {
            return;
        }

        Player blocker = (Player) event.getEntity();

        LivingEntity attacker = (LivingEntity) event.getDamager();

        if (!blocker.isBlocking()) {
            return;
        }

        if (!AntigriefManager.canInjure(blocker, attacker)) {
            return;
        }

        EcoEnchants.values().forEach(enchant -> {
            if (event.isCancelled()) {
                return;
            }

            if (!enchant.isEnabled()) {
                return;
            }

            if (enchant.getDisabledWorlds().contains(blocker.getWorld())) {
                return;
            }

            int level;

            if (!EnchantChecks.offhand(blocker, enchant) && !EnchantChecks.mainhand(blocker, enchant)) {
                return;
            }

            if (EnchantChecks.offhand(blocker, enchant)) {
                level = EnchantChecks.getOffhandLevel(blocker, enchant);
            } else {
                level = EnchantChecks.getMainhandLevel(blocker, enchant);
            }
            enchant.onDeflect(blocker, attacker, level, event);
        });
    }
}
