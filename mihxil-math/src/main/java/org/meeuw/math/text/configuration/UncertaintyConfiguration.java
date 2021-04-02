package org.meeuw.math.text.configuration;

import lombok.*;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * This configuration aspect defines how uncertainties must be formatted
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
public class UncertaintyConfiguration implements ConfigurationAspect {

    /**
     * Uncertainties can be formatted in several ways, each labeled with the appropriate {@link Notation} enum value.
     */
    @Getter
    @With
    private final Notation notation;

    @Getter
    @With
    private final double considerRoundingErrorFactor;

    @lombok.Builder
    private UncertaintyConfiguration(Notation notation,  double considerRoundingErrorFactor) {
        this.notation = notation;
        this.considerRoundingErrorFactor = considerRoundingErrorFactor;
    }

    public UncertaintyConfiguration() {
        this(Notation.PLUS_MINUS, 1000d);
    }


    public enum Notation {
        /**
         * Use a ± symbol between value and uncertainty
         */
        PLUS_MINUS,
        /**
         * User parentheses to indicate the uncertainty in the last displayed decimals.
         */
        PARENTHESES
    }
}
