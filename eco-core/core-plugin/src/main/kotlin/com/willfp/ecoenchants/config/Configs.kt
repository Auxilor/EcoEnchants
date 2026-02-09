package com.willfp.ecoenchants.config

import com.willfp.eco.core.config.BaseConfig
import com.willfp.eco.core.config.ConfigType
import com.willfp.ecoenchants.plugin

object TypesYml : BaseConfig("types", plugin, true, ConfigType.YAML)
object TargetsYml : BaseConfig("targets", plugin, true, ConfigType.YAML)
object RarityYml : BaseConfig("rarity", plugin, true, ConfigType.YAML)
object VanillaEnchantsYml : BaseConfig("vanillaenchants", plugin, false, ConfigType.YAML)
