package org.meeuw.math.text.configuration;

import lombok.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
public class UncertaintyConfiguration implements ConfigurationAspect {


    @lombok.Builder
    private UncertaintyConfiguration(Notation notation) {
        this.notation = notation;
    }

    public UncertaintyConfiguration() {
        this(Notation.PLUS_MINUS);
    }

    @Getter
    @With
    private final Notation notation;


    public enum Notation {
        PLUS_MINUS,
        PARENTHESES
    }
}
