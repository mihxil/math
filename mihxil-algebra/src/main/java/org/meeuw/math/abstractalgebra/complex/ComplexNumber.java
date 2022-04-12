/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber extends CompleteComplexNumber<ComplexNumber, RealNumber, RealField> {

    static final long serialVersionUID = 0L;

    public ComplexNumber(RealNumber real, RealNumber imaginary) {
        super(real, imaginary);
    }

    public static ComplexNumber of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    public static ComplexNumber of(RealNumber real) {
        return new ComplexNumber(real, ComplexNumbers.INSTANCE.getElementStructure().zero());
    }


    @Override
    protected ComplexNumber _of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public ComplexNumbers getStructure() {
        return ComplexNumbers.INSTANCE;
    }
}
