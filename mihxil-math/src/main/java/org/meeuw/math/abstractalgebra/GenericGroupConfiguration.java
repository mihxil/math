package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.With;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;

public class GenericGroupConfiguration implements ConfigurationAspect {


    @With
    @Getter
    private final Operator groupOperator;

    public GenericGroupConfiguration() {
        this(Operator.MULTIPLICATION);
    }

    @lombok.Builder
    private GenericGroupConfiguration(Operator groupOperator) {
        this.groupOperator = groupOperator;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(Group.class);
    }
}
