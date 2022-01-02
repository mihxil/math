package org.meeuw.math.text.configuration;

import lombok.*;

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

/**
 * The configuration aspect which specifies how numbers should be formatted.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
@EqualsAndHashCode
public class NumberConfiguration implements ConfigurationAspect {

    /**
     * If the absolute value of the exponent would be bigger than this, then
     * scientific notation will be used. Otherwise, no.
     *
     * This defaults to 4.
     */
    @Getter
    @With
    private final int minimalExponent;

    @Getter
    @With
    private final Thousands thousands;


    @lombok.Builder
    private NumberConfiguration(int minimalExponent, Thousands thousands) {
        this.minimalExponent = minimalExponent;
        this.thousands = thousands;
    }

    public NumberConfiguration() {
        this(4, Thousands.NONE);
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(UncertainDoubleFormatProvider.class);
    }

    enum Thousands {
        NONE,
        DOT,
        COMMA,
        LOCALE,
        UNDERSCORE,
        SPACE
    }
}
