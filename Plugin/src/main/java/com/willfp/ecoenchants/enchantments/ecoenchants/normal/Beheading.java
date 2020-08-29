package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.queue.DropQueue;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Beheading extends EcoEnchant {
    public Beheading() {
        super(
                new EcoEnchantBuilder("beheading", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.SWORD, Target.Applicable.AXE}, 4.0)
        );
    }
    // START OF LISTENERS

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if(event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();

        LivingEntity victim = event.getEntity();

        if(!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        if(Rand.randFloat(0, 1) > level * 0.01 * chance)
            return;

        ItemStack item;

        if(victim instanceof Player) {
            item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setOwningPlayer((Player) victim);
            item.setItemMeta(meta);
        } else {
            if(event.getEntityType().equals(EntityType.ZOMBIE)) {
                item = new ItemStack(Material.ZOMBIE_HEAD, 1);
            } else if(event.getEntityType().equals(EntityType.SKELETON)) {
                item = new ItemStack(Material.SKELETON_SKULL, 1);
            } else if(event.getEntityType().equals(EntityType.CREEPER)) {
                item = new ItemStack(Material.CREEPER_HEAD, 1);
            } else if(event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
                item = new ItemStack(Material.DRAGON_HEAD, 1);
            } else return;
        }

        new DropQueue(player)
                .addItem(item)
                .addXP(event.getDroppedExp())
                .setLocation(victim.getLocation())
                .push();

        event.setDroppedExp(0);
    }
}
