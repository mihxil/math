package org.meeuw.math.uncertainnumbers.field;


import java.util.Random;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.*;

/**
 * The field of {@link UncertainReal}'s
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
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
}
