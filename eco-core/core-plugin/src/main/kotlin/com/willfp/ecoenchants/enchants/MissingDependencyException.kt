package com.willfp.ecoenchants.enchants

import com.willfp.ecoenchants.EcoEnchantsPlugin

class MissingDependencyException(
    val plugins: List<String>
) : Exception() {
    override val message = "Missing the following plugins: ${plugins.joinToString(", ")}"
}

fun promptPluginInstall(plugin: EcoEnchantsPlugin, enchant: String, plugins: MutableList<String>) {
    if (!plugin.isLoaded) {
        return
    }

    val formatted = StringBuilder()

    when (plugins.size) {
        1 -> formatted.append(plugins.first())
        2 -> formatted.append(plugins[0])
            .append(" and ")
            .append(plugins[1])
        else -> {
            val last = plugins.removeLast()
            formatted.append(plugins.joinToString(", "))
                .append(", and ")
                .append(last)
        }
    }

    plugin.logger.apply {
        warning("Can't load the $enchant enchantment because you need $formatted installed")
        warning("Either download $formatted or delete the folders with their names (/plugins/EcoEnchants/enchants/) to remove this message!")
    }
}
