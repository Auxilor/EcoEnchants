package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.queue.DropQueue;
import com.willfp.ecoenchants.util.EqualIfOver;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
public class StoneSwitcher extends EcoEnchant {
    public StoneSwitcher() {
        super(
                new EcoEnchantBuilder("stone_switcher", EnchantmentType.NORMAL, Target.Applicable.PICKAXE, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void stoneSwitcherBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!HasEnchant.playerHeld(player, this)) return;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if(!block.getType().equals(Material.STONE)) return;

        if (event.isCancelled())
            return;

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        int level = HasEnchant.getPlayerLevel(player, this);
        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");

        if(Rand.randFloat(0, 1) > level * chance * 0.01)
            return;

        event.setDropItems(false);

        Material material;
        double random = Rand.randFloat(0, 1);
        double band = 1/(double) this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").size();
        int selectedIndex = (int) Math.floor(random/band);
        selectedIndex = EqualIfOver.equalIfOver(selectedIndex, this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").size() - 1);
        String materialName = this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blocks").get(selectedIndex);
        material = Material.getMaterial(materialName.toUpperCase());
        if(material == null) material = Material.COBBLESTONE;

        ItemStack item = new ItemStack(material, 1);

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItem(item)
                .push();
    }
}
