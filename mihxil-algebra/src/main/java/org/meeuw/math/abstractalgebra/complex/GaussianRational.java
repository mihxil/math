/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

/**
 * A Gaussian rational number is a complex number of the form p + qi, where p and q are both {@link RationalNumber rational numbers}
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
