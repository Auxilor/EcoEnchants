@file:Suppress("DEPRECATION", "KotlinRedundantDiagnosticSuppress")

package com.willfp.ecoenchants

import sun.misc.Unsafe
import java.lang.reflect.Field

private val unsafe = Unsafe::class.java.getDeclaredField("theUnsafe")
    .apply { isAccessible = true }
    .get(null) as Unsafe

fun Field.setStaticFinal(value: Any) {
    val offset = unsafe.staticFieldOffset(this)
    unsafe.putObject(unsafe.staticFieldBase(this), offset, value)
}
