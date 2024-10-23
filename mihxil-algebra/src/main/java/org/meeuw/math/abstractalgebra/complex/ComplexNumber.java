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
 */
package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static org.meeuw.math.abstractalgebra.complex.ComplexNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber extends CompleteComplexNumber<ComplexNumber, RealNumber, RealField> {

    private static final long serialVersionUID = 0L;

    public ComplexNumber(RealNumber real, RealNumber imaginary) {
        super(real, imaginary);
    }

    public static ComplexNumber of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    public static ComplexNumber of(RealNumber real) {
        return of(real, RealNumber.ZERO);
    }

    public static ComplexNumber of(double real, double imaginary) {
        return of(RealNumber.of(real), RealNumber.of(imaginary));
    }

    public static ComplexNumber real(RealNumber real) {
        return new ComplexNumber(real, INSTANCE.getElementStructure().zero());
    }

    public static ComplexNumber real(double real) {
        return real(RealNumber.of(real));
    }

    public static ComplexNumber imaginary(RealNumber imaginary) {
        return new ComplexNumber(INSTANCE.getElementStructure().zero(), imaginary);
    }


    @Override
    protected ComplexNumber _of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public ComplexNumbers getStructure() {
        return INSTANCE;
    }
}
