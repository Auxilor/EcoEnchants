package com.willfp.ecoenchants.config

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.BaseConfig
import com.willfp.eco.core.config.ConfigType

class TypesYml(plugin: EcoPlugin) : BaseConfig("types", plugin, true, ConfigType.YAML)
class TargetsYml(plugin: EcoPlugin) : BaseConfig("targets", plugin, true, ConfigType.YAML)
class RarityYml(plugin: EcoPlugin) : BaseConfig("rarity", plugin, true, ConfigType.YAML)
class VanillaEnchantsYml(plugin: EcoPlugin) : BaseConfig("vanillaenchants", plugin, false, ConfigType.YAML)
