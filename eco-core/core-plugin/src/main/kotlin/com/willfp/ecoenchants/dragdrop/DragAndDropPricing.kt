package com.willfp.ecoenchants.dragdrop

import com.willfp.ecoenchants.enchant.EcoEnchantLike
import org.bukkit.entity.Player
import wiki.ecohub.core.price.ConfiguredPrice
import wiki.ecohub.math.evaluateExpressionOrNull
import wiki.ecohub.ui.placeholder.placeholderContext

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

fun EcoEnchantLike.dragAndDropPriceDisplay(player: Player, level: Int): String {
    return dragAndDropPrice().getDisplay(player, dragAndDropPriceMultiplier(level))
}
