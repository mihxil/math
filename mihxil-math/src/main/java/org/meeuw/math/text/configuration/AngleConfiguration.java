package org.meeuw.math.text.configuration;

import lombok.*;

import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.numbers.Scalar;

@lombok.Builder
@ToString
@EqualsAndHashCode
public class AngleConfiguration implements ConfigurationAspect {

    @lombok.Builder.Default
    @With
    @Getter
    public final Unit unit = Unit.RADIANS;


    public AngleConfiguration(Unit unit) {
        this.unit = unit;
    }

    public AngleConfiguration() {
        this( Unit.RADIANS);
    }

    @Override
    public List<Class<?>> associatedWith() {
        return List.of(Scalar.class);
    }


    public enum Unit {
        RADIANS,
        DEGREES,
        TURNS
    }

    public static <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> String string(ScalarFieldElement<E, C> radians){
        return switch (ConfigurationService.getConfigurationAspect(AngleConfiguration.class).getUnit()) {
            case RADIANS -> "" + radians;
            case TURNS -> radians.complete().dividedBy(radians.getStructure().pi()).dividedBy(2) + " turns";
            case DEGREES -> radians.times(180L).complete().dividedBy(radians.getStructure().pi()) + "°";
        };
    }


}
