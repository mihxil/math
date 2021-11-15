package org.meeuw.math;

import java.lang.annotation.*;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Examples.class)
public @interface Example {

    Class<?> value();
}