package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class GaussianRationals extends AbstractComplexNumbers<GaussianRational, RationalNumber> implements Field<GaussianRational> {

    public static GaussianRationals INSTANCE = new GaussianRationals();

    GaussianRationals() {
        super(GaussianRational.class, RationalNumbers.INSTANCE);
    }

    @Override
    GaussianRational of(RationalNumber real, RationalNumber imaginary) {
        return new GaussianRational(real, imaginary);
    }


}
