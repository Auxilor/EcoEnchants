package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.EcoPlugin
import com.willfp.ecoenchants.EcoEnchantsPlugin

class MissingDependencyException(
    val plugins: Set<String>
) : Exception() {
    override val message = "Missing the following plugins: ${plugins.joinToString(", ")}"
}

// Plugin names mapped to enchants that aren't installed.
private val prompts = mutableMapOf<String, Int>()

fun addPluginPrompt(plugin: EcoEnchantsPlugin, plugins: Set<String>) {
    if (!plugin.isLoaded) {
        return
    }

    for (pluginName in plugins) {
        prompts[pluginName] = prompts.getOrDefault(pluginName, 0) + 1
    }
}

fun sendPrompts(plugin: EcoPlugin) {
    for ((pl, amount) in prompts) {
        plugin.logger.apply {
            warning("$amount enchantments were not loaded because they need $pl to be installed!")
            warning("Either download $pl or delete the folder at /plugins/EcoEnchants/enchants/${pl.lowercase()} to remove this message")
        }
    }

    prompts.clear()
}