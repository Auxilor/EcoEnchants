package com.willfp.ecoenchants.target

import com.willfp.ecoenchants.plugin
import wiki.ecohub.rules.holder.Dispatcher
import wiki.ecohub.rules.holder.HolderChange
import wiki.ecohub.rules.holder.ProvideContext
import wiki.ecohub.rules.holder.ProvidedHolder
import wiki.ecohub.rules.holder.ScopedHolderProvider

/**
 * Provides enchantment holders, except in worlds EcoEnchants is disabled in.
 *
 * Registered in place of a generic lambda so that libreforge stores its answers and asks it with
 * signal scopes, letting the finder re-check only the items that changed.
 */
internal object EnchantHolderProvider : ScopedHolderProvider {
    private val finder = EnchantFinder.toHolderProvider()

    override val id: String
        get() = finder.id

    override val invalidatedBy: Set<HolderChange>
        get() = finder.invalidatedBy

    override fun maxAge(dispatcher: Dispatcher<*>): Int? =
        finder.maxAge(dispatcher)

    override fun provide(dispatcher: Dispatcher<*>): Collection<ProvidedHolder> {
        if (plugin.isDisabledFor(dispatcher)) {
            return emptyList()
        }

        return finder.provide(dispatcher)
    }

    override fun provide(context: ProvideContext): Collection<ProvidedHolder> {
        if (plugin.isDisabledFor(context.dispatcher)) {
            return emptyList()
        }

        return (finder as? ScopedHolderProvider)?.provide(context)
            ?: finder.provide(context.dispatcher)
    }
}
