package com.willfp.ecoenchants.config

import com.willfp.ecoenchants.EcoEnchantsPlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object HardcodedEnchantMigration {
    private val migrations: Map<String, Migration> = mapOf(
        "permanence_curse" to Migration(emptyList()) { emptyList() },
        "soulbound" to Migration(listOf("single-use")) { config ->
            buildList {
                add(mapOf("id" to "keep_item"))

                if (config.getBoolean("single-use")) {
                    add(mapOf("id" to "remove_this_enchant", "triggers" to listOf("death")))
                }
            }
        },
        "repairing" to Migration(listOf("repair-per-level", "frequency", "not-while-holding")) { config ->
            val conditions = if (config.getBoolean("not-while-holding", true)) {
                listOf(
                    mapOf(
                        "id" to "provider_in_slot",
                        "args" to mapOf("slots" to listOf("hands", "armor"), "inverse" to true)
                    )
                )
            } else {
                emptyList()
            }

            listOf(
                mapOf(
                    "id" to "repair_item",
                    "args" to mapOf("damage" to "%level% * (${config["repair-per-level"] ?: 1})"),
                    "triggers" to listOf("static_${config.getInt("frequency", 30)}"),
                    "conditions" to conditions
                )
            )
        },
        "replenish" to Migration(listOf("consume-seeds", "only-fully-grown")) { config ->
            listOf(
                mapOf(
                    "id" to "replant_crops",
                    "args" to mapOf(
                        "consume_seeds" to config.getBoolean("consume-seeds", true),
                        "only_fully_grown" to config.getBoolean("only-fully-grown", false)
                    )
                )
            )
        }
    )

    fun migrate(plugin: EcoEnchantsPlugin) {
        plugin.dataFolder.resolve("enchants")
            .walk()
            .filter { it.isFile && it.extension == "yml" && it.nameWithoutExtension in migrations }
            .forEach { migrate(plugin, it) }
    }

    private fun migrate(plugin: EcoEnchantsPlugin, file: File) {
        val config = YamlConfiguration.loadConfiguration(file)

        if (config.contains("effects")) {
            return
        }

        val migration = migrations[file.nameWithoutExtension] ?: return

        config["effects"] = migration.effects(config)

        for (key in migration.legacyKeys) {
            config[key] = null
        }

        config.save(file)
        plugin.logger.info("Migrated ${file.name} to effects")
    }

    private class Migration(
        val legacyKeys: List<String>,
        val effects: (YamlConfiguration) -> List<Map<String, Any>>
    )
}
