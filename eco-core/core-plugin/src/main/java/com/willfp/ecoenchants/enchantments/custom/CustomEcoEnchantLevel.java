package com.willfp.ecoenchants.enchantments.custom;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.willfp.eco.core.config.BuildableConfig;
import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.eco.core.placeholder.StaticPlaceholder;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.libreforge.Holder;
import com.willfp.libreforge.conditions.Conditions;
import com.willfp.libreforge.conditions.ConfiguredCondition;
import com.willfp.libreforge.effects.ConfiguredEffect;
import com.willfp.libreforge.effects.Effects;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CustomEcoEnchantLevel implements Holder {
    /**
     * The parent EcoEnchant.
     */
    @Getter
    private final EcoEnchant parent;

    /**
     * The conditions.
     */
    private final Set<ConfiguredCondition> conditions = new HashSet<>();

    /**
     * The effects.
     */
    private final Set<ConfiguredEffect> effects = new HashSet<>();

    /**
     * The value placeholder.
     */
    @Getter
    private final String valuePlaceholder;

    /**
     * The level.
     */
    private final int level;

    /**
     * Create custom EcoEnchant level.
     *
     * @param parent The parent.
     * @param config The config.
     * @param level  The level.
     */
    public CustomEcoEnchantLevel(@NotNull final EcoEnchant parent,
                                 @NotNull final Config config,
                                 final int level) {
        this.parent = parent;
        this.level = level;

        config.injectPlaceholders(new StaticPlaceholder("level", () -> String.valueOf(level)));

        for (Config cfg : config.getSubsections("effects")) {
            effects.add(Effects.compile(cfg, "Custom EcoEnchant ID " + parent.getKey().getKey()));
        }

        for (Config cfg : config.getSubsections("conditions")) {
            conditions.add(Conditions.compile(cfg, "Custom EcoEnchant ID " + parent.getKey().getKey()));
        }

        conditions.add(Conditions.compile(
                new BuildableConfig()
                        .add("args.enchant", parent.getKey().getKey())
                        .add("id", "in_ecoenchant_world"),
                "EcoEnchants Internals (world) - If you see this message, report it as a bug!"
        ));

        conditions.add(Conditions.compile(
                new BuildableConfig()
                        .add("args.enchant", parent.getKey().getKey())
                        .add("id", "has_ecoenchant_requirements"),
                "EcoEnchants Internals (requirements) - If you see this message, report it as a bug!"
        ));

        this.valuePlaceholder = config.getString("value-placeholder");
    }

    @NotNull
    @Override
    public Set<ConfiguredCondition> getConditions() {
        return conditions;
    }

    @NotNull
    @Override
    public Set<ConfiguredEffect> getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return "CustomEcoEnchantLevel{"
                + "parent=" + parent
                + ",level=" + level
                + '}';
    }

    @NotNull
    @Override
    public List<String> getNotMetLines(@NotNull final Player player) {
        return NotMetLineCacheEntry.CACHE.get(new NotMetLineCacheEntry(player.getUniqueId(), this.getId()), (entry) -> {
            List<String> lines = new ArrayList<>();

            for (ConfiguredCondition condition : this.getConditions()) {
                if (!condition.isMet(player)) {
                    lines.addAll(Objects.requireNonNullElse(condition.getNotMetLines(), new ArrayList<>()));
                }
            }

            return lines;
        });
    }

    @NotNull
    @Override
    public String getId() {
        return this.parent.getKey().getKey() + "_" + this.level;
    }

    /**
     * Cache for not met lines.
     *
     * @param uuid     The UUID.
     * @param holderID The holder ID.
     */
    private record NotMetLineCacheEntry(@NotNull UUID uuid,
                                        @NotNull String holderID) {
        /**
         * The cache.
         */
        static final Cache<NotMetLineCacheEntry, List<String>> CACHE = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .build();
    }
}
