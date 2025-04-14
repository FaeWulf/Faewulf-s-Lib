package xyz.faewulf.lib.util.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SliderEntry {
    int min();      // Minimum value of the slider

    int max();      // Maximum value of the slider

    int step() default 1; // Optional step value, defaults to 1
}
