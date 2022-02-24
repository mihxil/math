package org.meeuw.math;

import java.lang.annotation.*;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Repeatable(Examples.class)
public @interface Example {

    Class<?> value();
}
