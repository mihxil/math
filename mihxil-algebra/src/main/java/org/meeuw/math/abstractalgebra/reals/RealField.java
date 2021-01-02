package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * The field of real numbers.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RealField extends AbstractAlgebraicStructure<RealNumber>
    implements CompleteField<RealNumber>, MetricSpace<RealNumber, RealNumber> {

    public static final RealField INSTANCE = new RealField();

    private RealField() {
        super(RealNumber.class);
    }

    @Override
    public RealNumber zero() {
        return RealNumber.ZERO;
    }

    @Override
    public RealNumber one() {
        return RealNumber.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    public RealNumber considerMultiplicationByZero(RealNumber r1, RealNumber r2) {
        double newValue = r1.value * r2.value;
        if (r1.isExactlyZero() || r2.isExactlyZero()) {
            return zero();
        } else if (r1.value == 0 || r2.value == 0) {
            return new RealNumber(newValue, Double.MAX_VALUE);
        } else if (Double.isNaN(r1.value) || Double.isNaN(r2.value)) {
            return new RealNumber(newValue, Double.MAX_VALUE);
        } else {
            if (newValue == 0) {
                return RealNumber.SMALLEST;
            }
            return new RealNumber(newValue,
                Math.abs(newValue) * (
                    Math.abs(r1.uncertainty / r1.value) +
                        Math.abs(r2.uncertainty / r2.value)) + uncertaintyForDouble(newValue)
            );
        }
    }


}
