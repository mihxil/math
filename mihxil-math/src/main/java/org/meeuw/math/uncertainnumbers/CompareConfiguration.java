package org.meeuw.math.uncertainnumbers;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.With;
import org.meeuw.configuration.ConfigurationAspect;

public class CompareConfiguration implements ConfigurationAspect {

    @With
    @Getter
    private boolean requiresEqualsTransitive;

    public CompareConfiguration() {
        this(true);
    }

    @lombok.Builder
    private CompareConfiguration(boolean requiresEqualsTransitive) {
        this.requiresEqualsTransitive = requiresEqualsTransitive;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(Uncertain.class);
    }
}
