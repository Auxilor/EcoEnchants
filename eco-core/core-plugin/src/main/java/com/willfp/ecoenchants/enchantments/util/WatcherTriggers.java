package com.willfp.ecoenchants.enchantments.util;

import com.google.common.collect.Sets;
import com.willfp.eco.core.proxy.ProxyFactory;
import com.willfp.eco.core.proxy.proxies.TridentStackProxy;
import com.willfp.eco.util.events.armorequip.ArmorEquipEvent;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.integrations.mcmmo.McmmoManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class WatcherTriggers extends PluginDependent implements Listener {
    protected WatcherTriggers(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event))
            return;
        if (!(event.getDamager() instanceof Arrow))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        if (((Arrow) event.getDamager()).getShooter() == null)
            return;
        if (!(((Arrow) event.getDamager()).getShooter() instanceof LivingEntity))
            return;

        LivingEntity attacker = (LivingEntity) ((Arrow) event.getDamager()).getShooter();
        Arrow arrow = (Arrow) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.hasMetadata("NPC")) return;

        if (attacker instanceof Player) {
            if (!AntigriefManager.canInjure((Player) attacker, victim)) return;
        }

        if (event.isCancelled()) return;

        EnchantChecks.getEnchantsOnArrow(arrow).forEach(((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(attacker.getWorld())) return;
            enchant.onArrowDamage(attacker, victim, arrow, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onTridentDamage(EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event))
            return;
        if (!(event.getDamager() instanceof Trident))
            return;

        if (!(((Trident) event.getDamager()).getShooter() instanceof LivingEntity))
            return;

        if (((Trident) event.getDamager()).getShooter() == null)
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (event.isCancelled())
            return;

        LivingEntity attacker = (LivingEntity) ((Trident) event.getDamager()).getShooter();
        Trident trident = (Trident) event.getDamager();
        ItemStack item = new ProxyFactory<>(TridentStackProxy.class).getProxy().getTridentStack(trident);

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.hasMetadata("NPC")) return;

        if (attacker instanceof Player) {
            if (!AntigriefManager.canInjure((Player) attacker, victim)) return;
        }

        EnchantChecks.getEnchantsOnItem(item).forEach(((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(attacker.getWorld())) return;
            enchant.onTridentDamage(attacker, victim, trident, level, event);
        }));
    }


    private static final Set<UUID> prevPlayersOnGround = Sets.newHashSet();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @EventHandler(ignoreCancelled = true)
    public void onJump(PlayerMoveEvent event) {
        if (McmmoManager.isFake(event))
            return;
        Player player = event.getPlayer();
        if (player.getVelocity().getY() > 0) {
            float jumpVelocity = 0.42f;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += ((float) player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
            }
            jumpVelocity = Float.parseFloat(df.format(jumpVelocity).replace(',', '.'));
            if (event.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!player.isOnGround() && Float.compare((float) player.getVelocity().getY(), jumpVelocity) == 0) {
                    EnchantChecks.getEnchantsOnArmor(player).forEach((enchant, level) -> {
                        if (event.isCancelled()) return;
                        if (!enchant.isEnabled()) return;
                        if (enchant.getDisabledWorlds().contains(player.getWorld())) return;
                        enchant.onJump(player, level, event);
                    });
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
        if (McmmoManager.isFake(event))
            return;
        if (!(event.getDamager() instanceof LivingEntity))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (event.isCancelled())
            return;

        LivingEntity attacker = (LivingEntity) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.hasMetadata("NPC")) return;

        if (attacker instanceof Player) {
            if (!AntigriefManager.canInjure((Player) attacker, victim)) return;
        }

        EnchantChecks.getEnchantsOnMainhand(attacker).forEach((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(attacker.getWorld())) return;
            enchant.onMeleeAttack(attacker, victim, level, event);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event) {
        if (McmmoManager.isFake(event))
            return;
        if (event.getProjectile().getType() != EntityType.ARROW)
            return;

        LivingEntity shooter = event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();

        EnchantChecks.getEnchantsOnMainhand(shooter).forEach((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) return;
            enchant.onBowShoot(shooter, arrow, level, event);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(EntityDamageEvent event) {
        if (McmmoManager.isFake(event))
            return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        EnchantChecks.getEnchantsOnArmor(victim).forEach((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(victim.getWorld())) return;
            enchant.onFallDamage(victim, level, event);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onArrowHit(ProjectileHitEvent event) {
        if (McmmoManager.isFake(event))
            return;
        if (!(event.getEntity().getShooter() instanceof LivingEntity))
            return;

        if (!(event.getEntity() instanceof Arrow)) return;

        if (event.getEntity().getShooter() == null)
            return;

        Arrow arrow = (Arrow) event.getEntity();
        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        EnchantChecks.getEnchantsOnArrow(arrow).forEach(((enchant, level) -> {
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) return;
            enchant.onArrowHit(shooter, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onTridentHit(ProjectileHitEvent event) {
        if (McmmoManager.isFake(event))
            return;
        if (!(event.getEntity().getShooter() instanceof LivingEntity))
            return;
        if (event.getEntity().getShooter() == null)
            return;

        if (!(event.getEntity() instanceof Trident)) return;

        Trident trident = (Trident) event.getEntity();
        ItemStack item = new ProxyFactory<>(TridentStackProxy.class).getProxy().getTridentStack(trident);
        LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();

        EnchantChecks.getEnchantsOnItem(item).forEach(((enchant, level) -> {
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) return;
            enchant.onTridentHit(shooter, level, event);
        }));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (McmmoManager.isFake(event))
            return;
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        if (event.isCancelled())
            return;

        EnchantChecks.getEnchantsOnMainhand(player).forEach((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(player.getWorld())) return;
            enchant.onBlockBreak(player, block, level, event);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageWearingArmor(EntityDamageEvent event) {
        if (McmmoManager.isFake(event))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        EnchantChecks.getEnchantsOnArmor(victim).forEach((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(victim.getWorld())) return;
            enchant.onDamageWearingArmor(victim, level, event);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorEquip(ArmorEquipEvent event) {
        if (McmmoManager.isFake(event))
            return;
        Player player = event.getPlayer();

        this.plugin.getScheduler().runLater(() -> {
            EcoEnchants.getAll().forEach((enchant -> {
                if (event.isCancelled()) return;
                if (!enchant.isEnabled()) return;
                if (enchant.getDisabledWorlds().contains(player.getWorld())) return;
                int level = EnchantChecks.getArmorPoints(player, enchant);
                enchant.onArmorEquip(player, level, event);
            }));
        }, 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageBlock(BlockDamageEvent event) {
        if (McmmoManager.isFake(event))
            return;
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (event.getBlock().getDrops(player.getInventory().getItemInMainHand()).isEmpty())
            return;

        EnchantChecks.getEnchantsOnMainhand(player).forEach((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(player.getWorld())) return;
            enchant.onDamageBlock(player, block, level, event);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onTridentLaunch(ProjectileLaunchEvent event) {
        if (McmmoManager.isFake(event))
            return;

        if (!(event.getEntity() instanceof Trident))
            return;

        if (!(event.getEntity().getShooter() instanceof LivingEntity))
            return;

        Trident trident = (Trident) event.getEntity();
        LivingEntity shooter = (LivingEntity) trident.getShooter();
        ItemStack item = new ProxyFactory<>(TridentStackProxy.class).getProxy().getTridentStack(trident);

        EnchantChecks.getEnchantsOnItem(item).forEach((enchant, level) -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(shooter.getWorld())) return;
            enchant.onTridentLaunch(shooter, trident, level, event);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onDeflect(EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof LivingEntity))
            return;

        Player blocker = (Player) event.getEntity();

        LivingEntity attacker = (LivingEntity) event.getDamager();

        if (!blocker.isBlocking()) return;

        if (!AntigriefManager.canInjure(blocker, attacker)) return;

        EcoEnchants.getAll().forEach((enchant -> {
            if (event.isCancelled()) return;
            if (!enchant.isEnabled()) return;
            if (enchant.getDisabledWorlds().contains(blocker.getWorld())) return;
            int level;
            if (!EnchantChecks.offhand(blocker, enchant) && !EnchantChecks.mainhand(blocker, enchant)) return;
            if (EnchantChecks.offhand(blocker, enchant)) level = EnchantChecks.getOffhandLevel(blocker, enchant);
            else level = EnchantChecks.getMainhandLevel(blocker, enchant);
            enchant.onDeflect(blocker, attacker, level, event);
        }));
    }
}
