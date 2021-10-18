package com.willfp.ecoenchants.data.storage

import com.willfp.eco.core.config.yaml.YamlBaseConfig
import com.willfp.ecoenchants.EcoEnchantsPlugin
import java.util.*

@Suppress("UNCHECKED_CAST")
class YamlDataHandler(
    plugin: EcoEnchantsPlugin
) : DataHandler {
    private val dataYml = DataYml(plugin)

    override fun save() {
        dataYml.save()
    }

    override fun <T> write(uuid: UUID, key: String, value: T) {
        dataYml.set("player.$uuid.$key", value)
    }

    override fun <T> read(uuid: UUID, key: String): T? {
        return dataYml.get("player.$uuid.$key") as T?
    }

    class DataYml(
        plugin: EcoEnchantsPlugin
    ) : YamlBaseConfig(
        "data",
        false,
        plugin
    )
}