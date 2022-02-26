package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.With;

import java.util.*;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;

public class RandomConfiguration implements ConfigurationAspect {


    public static long nextLong(Random random) {
        int setSize = ConfigurationService.getConfigurationAspect(RandomConfiguration.class).getSetSize();
        return random.nextInt(setSize - setSize / 2);
    }

    public static long nextNonNegativeLong(Random random) {
        int setSize = ConfigurationService.getConfigurationAspect(RandomConfiguration.class).getSetSize();
        return random.nextInt(setSize);
    }

    @With
    @Getter
    private final int setSize;

    public RandomConfiguration() {
        this(1000);
    }

    @lombok.Builder
    private RandomConfiguration(int setSize) {
        this.setSize = setSize;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(AlgebraicStructure.class);
    }
}
