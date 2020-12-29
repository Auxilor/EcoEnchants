package com.willfp.eco.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.willfp.eco.util.optional.Prerequisite;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@UtilityClass
public class TeamUtils {
    /**
     * Ore ChatColors.
     */
    private static final BiMap<Material, ChatColor> MATERIAL_COLORS = HashBiMap.create();

    /**
     * All chat color teams.
     */
    private static final BiMap<ChatColor, Team> CHAT_COLOR_TEAMS = HashBiMap.create();

    /**
     * The server scoreboard.
     */
    private static final Scoreboard SCOREBOARD = Bukkit.getScoreboardManager().getMainScoreboard();

    /**
     * Get team from {@link ChatColor}.
     * <p>
     * For {@link org.bukkit.potion.PotionEffectType#GLOWING}.
     *
     * @param color The color to find the team for.
     * @return The team.
     */
    public Team fromChatColor(@NotNull final ChatColor color) {
        if (CHAT_COLOR_TEAMS.containsKey(color)) {
            return CHAT_COLOR_TEAMS.get(color);
        }

        Team team;

        if (!SCOREBOARD.getTeams().stream().map(Team::getName).collect(Collectors.toList()).contains("EE-" + color.name())) {
            team = SCOREBOARD.registerNewTeam("EE-" + color.name());
        } else {
            team = SCOREBOARD.getTeam("EE-" + color.name());
        }
        assert team != null;
        team.setColor(color);
        CHAT_COLOR_TEAMS.forcePut(color, team);

        return team;
    }

    /**
     * Get team from material.
     * <p>
     * For {@link org.bukkit.potion.PotionEffectType#GLOWING}.
     *
     * @param material The material to find the team from.
     * @return The team.
     */
    public Team getMaterialColorTeam(@NotNull final Material material) {
        return fromChatColor(MATERIAL_COLORS.getOrDefault(material, ChatColor.WHITE));
    }

    static {
        for (ChatColor value : ChatColor.values()) {
            fromChatColor(value);
        }

        MATERIAL_COLORS.forcePut(Material.COAL_ORE, ChatColor.BLACK);
        MATERIAL_COLORS.forcePut(Material.IRON_ORE, ChatColor.GRAY);
        MATERIAL_COLORS.forcePut(Material.GOLD_ORE, ChatColor.YELLOW);
        MATERIAL_COLORS.forcePut(Material.LAPIS_ORE, ChatColor.BLUE);
        MATERIAL_COLORS.forcePut(Material.REDSTONE_ORE, ChatColor.RED);
        MATERIAL_COLORS.forcePut(Material.DIAMOND_ORE, ChatColor.AQUA);

        if (Prerequisite.MINIMUM_1_16.isMet()) {
            MATERIAL_COLORS.forcePut(Material.ANCIENT_DEBRIS, ChatColor.DARK_RED);
        }
    }
}
