package org.meeuw.jupiter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.meeuw.math.text.configuration.AngleConfiguration;

/**
 * Configures {@link org.meeuw.math.text.configuration.NumberConfiguration} and optionally
 * also {@link org.meeuw.math.text.configuration.AngleConfiguration},
 * {@code org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberConfiguration}
 * @since 0.19
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WithNumberConfiguration {

    int maxPrecision() default 50;

    int maxScale() default 10_000;


    AngleConfiguration.Unit angles() default AngleConfiguration.Unit.DEGREES;

    /**
     * See org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberConfiguration
     * @return
     */
    String rationals() default "INTEGER_AND_FRACTION";

}
