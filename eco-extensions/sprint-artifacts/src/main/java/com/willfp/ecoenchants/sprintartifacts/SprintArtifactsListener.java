package com.willfp.ecoenchants.sprintartifacts;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class SprintArtifactsListener implements Listener {
    @EventHandler
    public void onPlayerMove(@NotNull final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.isSprinting()) {
            return;
        }

        if (!player.isOnGround()) {
            return;
        }

        ItemStack boots = player.getInventory().getBoots();
        if (boots == null) {
            return;
        }
        ItemMeta bootsMeta = boots.getItemMeta();
        if (bootsMeta == null) {
            return;
        }

        Optional<Artifact> matching = bootsMeta.getEnchants().keySet().stream()
                .filter(enchantment -> enchantment instanceof Artifact)
                .map(enchant -> (Artifact) enchant)
                .findFirst();
        if (matching.isEmpty()) {
            return;
        }
        Artifact artifact = matching.get();

        if (!artifact.areRequirementsMet(player)) {
            return;
        }

        if (!EnchantChecks.boots(player, artifact)) {
            return;
        }

        if (artifact.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        player.getWorld().spawnParticle(artifact.getParticle(), player.getLocation().add(0, 0.1, 0), 1, 0, 0, 0, 0, artifact.getDustOptions(), true);
    }
}
