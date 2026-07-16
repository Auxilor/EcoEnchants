package com.willfp.ecoenchants.dragdrop

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class EnchantLevelMergeTests {
    @Test
    fun equalLevelsBumpByOne() {
        assertEquals(3, mergeDragAndDropLevel(2, 2, 5), "equal -> +1")
    }

    @Test
    fun equalLevelsAtMaxStay() {
        assertEquals(5, mergeDragAndDropLevel(5, 5, 5), "equal at max stays")
    }

    @Test
    fun differentLevelsTakeTheHigher() {
        assertEquals(4, mergeDragAndDropLevel(4, 2, 5), "existing higher")
        assertEquals(4, mergeDragAndDropLevel(2, 4, 5), "incoming higher")
    }
}
