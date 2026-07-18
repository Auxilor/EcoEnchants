package com.willfp.ecoenchants.dragdrop

fun mergeDragAndDropLevel(existing: Int, incoming: Int, maxLevel: Int): Int {
    return if (existing == incoming) {
        minOf(maxLevel, existing + 1)
    } else {
        maxOf(existing, incoming)
    }
}
