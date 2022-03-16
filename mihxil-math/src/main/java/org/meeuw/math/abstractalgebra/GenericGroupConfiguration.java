package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.With;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

public class GenericGroupConfiguration implements ConfigurationAspect {


    @With
    @Getter
    private final BasicAlgebraicBinaryOperator groupOperator;

    public GenericGroupConfiguration() {
        this(BasicAlgebraicBinaryOperator.MULTIPLICATION);
    }

    @lombok.Builder
    private GenericGroupConfiguration(BasicAlgebraicBinaryOperator groupOperator) {
        this.groupOperator = groupOperator;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(Group.class);
    }
}
