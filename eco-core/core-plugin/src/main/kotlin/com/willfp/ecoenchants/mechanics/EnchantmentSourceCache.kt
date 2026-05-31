package com.willfp.ecoenchants.mechanics

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import java.util.concurrent.ThreadLocalRandom

internal object EnchantmentSourceCache {
    private var loaded = false
    private var enchantingCache = emptyList<EcoEnchant>()
    private var tradingCache = emptyList<EcoEnchant>()
    private var discoveryCache = emptyList<EcoEnchant>()

    val enchanting: List<EcoEnchant>
        get() {
            ensureLoaded()
            return enchantingCache
        }

    val trading: List<EcoEnchant>
        get() {
            ensureLoaded()
            return tradingCache
        }

    val discovery: List<EcoEnchant>
        get() {
            ensureLoaded()
            return discoveryCache
        }

    fun reload() {
        val enchantments = EcoEnchants.values().toList()
        enchantingCache = enchantments.filter { it.isObtainableThroughEnchanting }
        tradingCache = enchantments.filter { it.isObtainableThroughTrading }
        discoveryCache = enchantments.filter { it.isObtainableThroughDiscovery }
        loaded = true
    }

    private fun ensureLoaded() {
        if (!loaded) {
            reload()
        }
    }
}

internal fun List<EcoEnchant>.randomizedIteration(): Iterable<EcoEnchant> {
    if (this.size < 2) {
        return this
    }

    val source = this

    return Iterable {
        val size = source.size
        val random = ThreadLocalRandom.current()
        val start = random.nextInt(size)
        val step = randomCoprimeStep(size, random)

        object : Iterator<EcoEnchant> {
            private var visited = 0

            override fun hasNext(): Boolean = visited < size

            override fun next(): EcoEnchant {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }

                val index = (start + visited * step) % size
                visited++
                return source[index]
            }
        }
    }
}

private fun randomCoprimeStep(size: Int, random: ThreadLocalRandom): Int {
    var step = random.nextInt(1, size)

    while (gcd(step, size) != 1) {
        step = random.nextInt(1, size)
    }

    return step
}

private fun gcd(a: Int, b: Int): Int {
    var x = a
    var y = b

    while (y != 0) {
        val next = x % y
        x = y
        y = next
    }

    return x
}
