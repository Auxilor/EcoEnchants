package com.willfp.ecoenchants.enchants

import com.willfp.ecoenchants.EcoEnchantsPlugin

class MissingDependencyException(
    val plugins: List<String>
) : Exception() {
    override val message = "Missing the following plugins: ${plugins.joinToString(", ")}"
}

// Plugin names mapped to enchants not installed.
private val prompts = mutableMapOf<String, Int>()

fun addPluginPrompt(plugin: EcoEnchantsPlugin, plugins: MutableList<String>) {
    if (!plugin.isLoaded) {
        return
    }

    for (pluginName in plugins) {
        prompts[pluginName] = prompts.getOrDefault(pluginName, 0) + 1
    }
}

fun sendPrompts(plugin: EcoEnchantsPlugin) {
    for ((pl, amount) in prompts) {
        plugin.logger.apply {
            warning("$amount enchantments were not loaded because they need $pl to be installed")
            warning("Either download $pl or delete the folder with their names (/plugins/EcoEnchants/enchants/) to remove this message!")
        }
    }

    prompts.clear()
}
