package com.willfp.ecoenchants.dragdrop

import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.eco.core.price.ConfiguredPrice
import com.willfp.eco.util.evaluateExpressionOrNull
import com.willfp.ecoenchants.enchant.EcoEnchantLike

fun EcoEnchantLike.isDragAndDropEnabled(): Boolean {
    return config.getBool("drag-and-drop.enabled")
}

fun EcoEnchantLike.dragAndDropPrice(): ConfiguredPrice {
    return ConfiguredPrice.createOrFree(config.getSubsection("drag-and-drop.price"))
}

fun EcoEnchantLike.dragAndDropPriceMultiplier(level: Int): Double {
    val expression = config.getStringOrNull("drag-and-drop.price-level-multiplier") ?: return 1.0

    return evaluateExpressionOrNull(
        expression.replace("%level%", level.toString()),
        placeholderContext()
    ) ?: 1.0
}
