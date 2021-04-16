package com.willfp.ecoskills.classes;

import com.willfp.eco.core.gui.menu.Menu;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ClassesMenu {
    /**
     * The classes menu.
     */
    @Getter
    private static Menu menu;

    /**
     * Set the menu for /classes.
     *
     * @param menu The menu.
     */
    public void setMenu(@NotNull final Menu menu) {
        ClassesMenu.menu = menu;
    }

    /**
     * Show the menu to a player.
     *
     * @param player The player.
     * @return The inventory.
     */
    public Inventory showMenu(@NotNull final Player player) {
        return menu.open(player);
    }
}
