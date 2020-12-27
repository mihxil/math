package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;

/**
 *  A Gaussian rational number is a complex number of the form p + qi, where p and q are both rational numbers
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class GaussianRational extends AbstractComplexNumber<GaussianRational, RationalNumber> {

    public GaussianRational(RationalNumber real, RationalNumber imaginary) {
        super(real, imaginary);
    }

    @Override
    public GaussianRationals getStructure() {
        return GaussianRationals.INSTANCE;
    }

    @Override
    public GaussianRational of(RationalNumber real, RationalNumber imaginary){
        return new GaussianRational(real, imaginary);
    }


}
