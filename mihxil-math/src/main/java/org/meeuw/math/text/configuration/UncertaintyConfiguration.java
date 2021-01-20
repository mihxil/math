package org.meeuw.math.text.configuration;

import lombok.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@ToString
public class UncertaintyConfiguration implements Configuration {


    @lombok.Builder
    public UncertaintyConfiguration(Notation notation) {
        this.notation = notation;
    }

    @Getter
    @With
    private final Notation notation;


    public enum Notation {
        PLUS_MINUS,
        PARENTHESES
    }
}
