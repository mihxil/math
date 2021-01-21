package org.meeuw.math.text.configuration;

import lombok.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
@EqualsAndHashCode
public class NumberConfiguration implements ConfigurationAspect {

    @Getter
    @With
    private final int minimalExponent;

    @lombok.Builder
    private NumberConfiguration(int minimalExponent) {
        this.minimalExponent = minimalExponent;
    }

    public NumberConfiguration() {
        this(4);
    }
}
