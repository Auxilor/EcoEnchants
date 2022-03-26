package com.willfp.ecoenchants.display.options;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.ecoenchants.command.CommandToggleDescriptions;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DescriptionOptions extends PluginDependent<EcoPlugin> {
    /**
     * The threshold below which to describe enchantments.
     */
    @Getter
    private int threshold;

    /**
     * If the options are enabled.
     */
    @Getter
    private boolean enabled;

    /**
     * The description lines color.
     */
    @Getter
    private String color;

    /**
     * If enchantment descriptions should be at the bottom of the enchantment lore rather than under each enchantment.
     */
    @Getter
    private boolean showingAtBottom;

    /**
     * If descriptions should only be shown on books.
     */
    @Getter
    private boolean onlyOnBooks;

    /**
     * Create new description options.
     *
     * @param plugin EcoEnchants.
     */
    public DescriptionOptions(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Update the options.
     */
    public void update() {
        threshold = this.getPlugin().getConfigYml().getInt("lore.describe.before-lines");
        enabled = this.getPlugin().getConfigYml().getBool("lore.describe.enabled");
        color = this.getPlugin().getLangYml().getFormattedString("description-color");
        showingAtBottom = this.getPlugin().getConfigYml().getBool("lore.describe.at-bottom");
        onlyOnBooks = this.getPlugin().getConfigYml().getBool("lore.describe.only-on-books");
    }

    /**
     * Get if descriptions are enabled for a player.
     *
     * @param player The player.
     * @return If enabled.
     */
    public boolean enabledForPlayer(@Nullable final Player player) {
        if (player == null) {
            return true;
        }

        return PlayerProfile.load(player).read(CommandToggleDescriptions.DESCRIPTIONS_KEY);
    }
}
