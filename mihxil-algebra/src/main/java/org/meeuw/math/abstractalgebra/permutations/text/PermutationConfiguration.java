package org.meeuw.math.abstractalgebra.permutations.text;

import lombok.Getter;
import lombok.With;

import org.meeuw.configuration.ConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationConfiguration implements ConfigurationAspect {

    @With
    @Getter
    private final Notation notation;

    @With
    @Getter
    private final Offset offset;


    public PermutationConfiguration() {
        this(Notation.CYCLES, Offset.ONE);
    }

    @lombok.Builder
    private PermutationConfiguration(Notation notation, Offset offset) {
        this.notation = notation;
        this.offset = offset;
    }
}
