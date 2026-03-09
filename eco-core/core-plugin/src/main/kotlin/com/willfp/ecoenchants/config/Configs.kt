package com.willfp.ecoenchants.config

import com.willfp.eco.core.config.BaseConfig
import com.willfp.eco.core.config.ConfigType
import com.willfp.ecoenchants.EcoEnchantsPlugin

class TypesYml(plugin: EcoEnchantsPlugin) : BaseConfig("types", plugin, true, ConfigType.YAML)
class TargetsYml(plugin: EcoEnchantsPlugin) : BaseConfig("targets", plugin, true, ConfigType.YAML)
class RarityYml(plugin: EcoEnchantsPlugin) : BaseConfig("rarity", plugin, true, ConfigType.YAML)
class VanillaEnchantsYml(plugin: EcoEnchantsPlugin) : BaseConfig("vanillaenchants", plugin, false, ConfigType.YAML)
