package org.meeuw.math;

import java.lang.annotation.*;

/**
 * Marker for operator methods, to indicate that they are supported but non algebraicly. I.e. they may not always be
 * allowed (e.g. you cannot add any two physical numbers) or they result an object not from the same algebra (e.g. the absolute value from the algebra of 'negative numbers' can be taken, but is not an negative number.
 *
 * @since 0.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NonAlgebraic {
}
