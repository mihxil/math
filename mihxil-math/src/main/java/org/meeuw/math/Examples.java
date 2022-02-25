package org.meeuw.math;

import java.lang.annotation.*;

/**
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Examples {

    Example[] value();
}
