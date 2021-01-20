package org.meeuw.math.text.configuration;

import lombok.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
public class NumberConfiguration implements Configuration {

    @Getter
    @With
    private final int minimalExponent;

    @lombok.Builder
    public NumberConfiguration(int minimalExponent) {
        this.minimalExponent = minimalExponent;
    }
}
