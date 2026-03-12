package org.meeuw.jupiter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.meeuw.math.text.configuration.UncertaintyConfiguration;

/**
 * Configures {@link org.meeuw.math.text.configuration.UncertaintyConfiguration} to use
 * the {@link org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation#ROUND_VALUE} notation, and to explicitly strip zeros.
 * This is convenient for testing, as it makes the output more compact
 * @since 0.19
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SetUncertaintyConfiguration {

     boolean stripZeros() default true;

     UncertaintyConfiguration.Notation notation() default UncertaintyConfiguration.Notation.ROUND_VALUE;
}
