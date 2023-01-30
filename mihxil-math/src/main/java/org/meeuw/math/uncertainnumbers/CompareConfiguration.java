package org.meeuw.math.uncertainnumbers;

import lombok.Getter;
import lombok.With;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

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
}
