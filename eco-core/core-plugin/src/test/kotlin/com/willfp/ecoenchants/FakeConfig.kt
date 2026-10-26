package com.willfp.ecoenchants

import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.StringUtils

internal class FakeConfig(private val values: Map<String, Any?> = emptyMap()) : Config {
    override fun toPlaintext(): String = ""

    override fun has(path: String): Boolean = values.containsKey(path)

    override fun getKeys(deep: Boolean): List<String> = values.keys.toList()

    override fun get(path: String): Any? = values[path]

    override fun set(path: String, `object`: Any?) {
        throw UnsupportedOperationException("FakeConfig is read-only")
    }

    override fun getSubsectionOrNull(path: String): Config? {
        val prefix = "$path."
        val nested = values.filterKeys { it.startsWith(prefix) }
            .mapKeys { (key, _) -> key.removePrefix(prefix) }
        return if (nested.isEmpty()) null else FakeConfig(nested)
    }

    override fun getIntOrNull(path: String): Int? = values[path] as? Int

    override fun getIntsOrNull(path: String): List<Int>? = null

    override fun getBoolOrNull(path: String): Boolean? = values[path] as? Boolean

    override fun getBoolsOrNull(path: String): List<Boolean>? = null

    override fun getStringOrNull(path: String, format: Boolean, option: StringUtils.FormatOption): String? =
        values[path] as? String

    override fun getStringsOrNull(path: String, format: Boolean, option: StringUtils.FormatOption): List<String>? =
        null

    override fun getDoubleOrNull(path: String): Double? = values[path] as? Double

    override fun getDoublesOrNull(path: String): List<Double>? = null

    override fun getSubsectionsOrNull(path: String): List<Config>? = null

    override fun getType(): ConfigType = ConfigType.JSON

    override fun clone(): Config = FakeConfig(values)
}
