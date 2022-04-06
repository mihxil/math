package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

/**
 * A Gaussian rational number is a complex number of the form p + qi, where p and q are both {@link RationalNumber}s
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class GaussianRational extends AbstractComplexNumber<GaussianRational, RationalNumber, RationalNumbers> {


    public static GaussianRational of(RationalNumber real, RationalNumber imaginary) {
        return new GaussianRational(real, imaginary);
    }

    public static GaussianRational of(RationalNumber real) {
        return new GaussianRational(real, GaussianRationals.INSTANCE.getElementStructure().zero());
    }

    public GaussianRational(RationalNumber real, RationalNumber imaginary) {
        super(real, imaginary);
    }

    @Override
    public GaussianRationals getStructure() {
        return GaussianRationals.INSTANCE;
    }

    @Override
    public GaussianRational _of(RationalNumber real, RationalNumber imaginary){
        return new GaussianRational(real, imaginary);
    }
}
