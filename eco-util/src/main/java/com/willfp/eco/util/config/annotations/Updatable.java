package com.willfp.eco.util.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Updatable {
    /**
     * The static, no-parameter method to be called on config update.
     * <p>
     * Default is <b>update</b>.
     *
     * @return The method name.
     */
    String methodName() default "update";
}
