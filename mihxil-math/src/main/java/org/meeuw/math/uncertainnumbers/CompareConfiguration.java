package org.meeuw.math.uncertainnumbers;

import lombok.Getter;
import lombok.With;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;

public class CompareConfiguration implements ConfigurationAspect {

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
     * Run a piece of code with {@link #isEqualsIsStrict()} {@code false}
     *
     * E.g. used much used in tests.
     */
    public static void withLooseEquals(Runnable r) {
        ConfigurationService.withAspect(CompareConfiguration .class, compareConfiguration -> compareConfiguration.withEqualsIsStrict(false), r);
    }
}
