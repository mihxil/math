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

    @Override
    public String toString() {
        return "ℝ";
    }

    public RealNumber considerMultiplicationBySpecialValues(RealNumber r1, RealNumber r2) {
        double newValue = r1.value * r2.value;
        // multiplication by zero
        if (r1.isExactlyZero() || r2.isExactlyZero()) {
            return zero();
        } else if (r1.value == 0 && r2.value != 0) {
            return new RealNumber(newValue, r1.uncertainty);
        } else if (r2.value == 0 && r1.value != 0) {
            return new RealNumber(newValue, r2.uncertainty);
            // NaN
        } else if (Double.isNaN(r1.value) || Double.isNaN(r2.value)) {
            return new RealNumber(newValue, Double.NaN);
        } else {
            return new RealNumber(newValue,
                Math.abs(newValue) * (
                    Math.abs(r1.uncertainty / (r1.value + r1.uncertainty)) +
                        Math.abs(r2.uncertainty / (r2.value + r2.uncertainty))) + uncertaintyForDouble(newValue)

            );
        }
    }


}
