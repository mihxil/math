package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * The field of real numbers.
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public class HyperRealField extends AbstractAlgebraicStructure<HyperRealNumber>
    implements Field<HyperRealNumber> {

    public static final HyperRealField INSTANCE = new HyperRealField();

    private HyperRealField() {
        super(HyperRealNumber.class);
    }


    @Override
    public HyperRealNumber zero() {
        return HyperRealNumber.ZERO;
    }

    @Override
    public HyperRealNumber one() {
        return HyperRealNumber.ONE;
    }

    public HyperRealNumber omega() {
        return HyperRealNumber.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public String toString() {
        return "*ℝ";
    }

    public HyperRealNumber considerMultiplicationBySpecialValues(HyperRealNumber r1, HyperRealNumber r2) {
        double newValue = r1.value * r2.value;
        // multiplication by zero
        if (r1.isExactlyZero() || r2.isExactlyZero()) {
            return zero();
        } else if (r1.value == 0 && r2.value != 0) {
            return new HyperRealNumber(newValue, r1.uncertainty);
        } else if (r2.value == 0 && r1.value != 0) {
            return new HyperRealNumber(newValue, r2.uncertainty);
            // NaN
        } else if (Double.isNaN(r1.value) || Double.isNaN(r2.value)) {
            return new HyperRealNumber(newValue, Double.NaN);
        } else {
            return new HyperRealNumber(newValue,
                Math.abs(newValue) * (
                    Math.abs(r1.uncertainty / (r1.value + r1.uncertainty)) +
                        Math.abs(r2.uncertainty / (r2.value + r2.uncertainty))) + uncertaintyForDouble(newValue)

            );
        }
    }


}
