package org.meeuw.math.uncertainnumbers;

import lombok.*;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * A configuration aspects related to comparing {@link Uncertain uncertain} numbers.
 */
@ToString
public class CompareConfiguration implements ConfigurationAspect {

    /**
     * Whether {@link Object#equals} must be {@link Uncertain#strictlyEquals(Object) strict}, i.e. preserve transitivity, or {@link org.meeuw.math.abstractalgebra.AbstractAlgebraicElement#eq(AlgebraicElement) not} (allowing for some 'uncertainty' in the equals,  but breaking transitivity).
     */
    @With
    @Getter
    private boolean equalsIsStrict;

    public CompareConfiguration() {
        this(true);
    }

    @lombok.Builder
    private CompareConfiguration(boolean equalsIsStrict) {
        this.equalsIsStrict = equalsIsStrict;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(Uncertain.class);
    }

    /**
     * Run a piece of code with {@link #isEqualsIsStrict()} {@code false}.
     * <p>
     * E.g. used much used in tests.
     */
    public static void withLooseEquals(Runnable r) {
        ConfigurationService.withAspect(CompareConfiguration .class, compareConfiguration -> compareConfiguration.withEqualsIsStrict(false), r);
    }
}
