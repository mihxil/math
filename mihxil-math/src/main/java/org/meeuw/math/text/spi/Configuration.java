package org.meeuw.math.text.spi;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class Configuration {

    public enum UncertaintyNotation {
        PLUS_MINUS,
        PARENTHESES
    }

    @Getter
    private final int minimalExponent;

    @Getter
    private final UncertaintyNotation uncertaintyNotation;

    @lombok.Builder(toBuilder = true)
    private  Configuration(Integer minimalExponent, UncertaintyNotation uncertaintyNotation) {
        this.minimalExponent = minimalExponent == null ? 4 : minimalExponent;
        this.uncertaintyNotation = uncertaintyNotation == null ? UncertaintyNotation.PLUS_MINUS : uncertaintyNotation;
    }

}
