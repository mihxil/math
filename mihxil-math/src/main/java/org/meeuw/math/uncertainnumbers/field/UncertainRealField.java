package org.meeuw.math.uncertainnumbers.field;


import java.util.Random;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.Example;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;

/**
 * The field of {@link UncertainReal}'s
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(CompleteScalarField.class)
public class UncertainRealField
    extends AbstractAlgebraicStructure<UncertainReal>
    implements CompleteScalarField<UncertainReal> {

    public static final UncertainRealField INSTANCE = new UncertainRealField();

    private UncertainRealField() {
        super(UncertainReal.class);
    }

    @Override
    public UncertainReal zero() {
        return UncertainDoubleElement.ZERO;
    }

    @Override
    public UncertainReal one() {
        return UncertainDoubleElement.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public UncertainReal nextRandom(Random random) {
        double value = ConfigurationService.getConfigurationAspect(RandomConfiguration.class).getSetSize() * (random.nextDouble() - 0.5d);
        return new UncertainDoubleElement(value, Math.abs(value * random.nextDouble()));
    }

    @Override
    public String toString() {
        return "ℝᵤ"; // ᵤ: uncertain values
    }

    @Override
    public UncertainReal pi() {
        return UncertainDoubleElement.of(Math.PI, Utils.uncertaintyForDouble(Math.PI));
    }

    @Override
    public UncertainReal e() {
        return UncertainDoubleElement.of(Math.E, Utils.uncertaintyForDouble(Math.E));
    }
}
