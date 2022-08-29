package com.willfp.ecoenchants.enchants

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.ExtendableConfig

class EnchantmentConfig(
    name: String,
    source: Class<*>,
    plugin: EcoPlugin
) : ExtendableConfig(
    name,
    true,
    plugin,
    source,
    "enchants/",
    ConfigType.YAML
)
