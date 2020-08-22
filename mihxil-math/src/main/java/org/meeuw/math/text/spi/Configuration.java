package org.meeuw.math.text.spi;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Configuration {

    @Getter
    private final int minimalExponent;

    @lombok.Builder
    public Configuration(int minimalExponent) {
        this.minimalExponent = minimalExponent;
    }
}
