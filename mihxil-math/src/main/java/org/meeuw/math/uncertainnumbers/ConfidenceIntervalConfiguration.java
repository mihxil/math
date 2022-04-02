package org.meeuw.math.uncertainnumbers;

import lombok.Getter;
import lombok.With;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

public class ConfidenceIntervalConfiguration implements ConfigurationAspect {


    @With
    @Getter
    private final float sds;

    public ConfidenceIntervalConfiguration() {
        this(1f);
    }

    @lombok.Builder
    private ConfidenceIntervalConfiguration(float sds) {
        this.sds = sds;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(ConfidenceInterval.class);
    }
}
