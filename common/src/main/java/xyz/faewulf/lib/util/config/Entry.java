package xyz.faewulf.lib.util.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Entry {
    String name();     // Config name to use in the file

    String category(); // Category to group similar configs

    String group() default "Others"; // Category to group similar configs

    String info() default "";  // Extra information or tooltip

    boolean hidden() default false;

    boolean require_restart() default false;
}