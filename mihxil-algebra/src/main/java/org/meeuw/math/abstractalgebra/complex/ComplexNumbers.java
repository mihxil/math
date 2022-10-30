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

import lombok.extern.java.Log;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.text.TextUtils;

/**
 * The {@link Field} of {@link ComplexNumber}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
@Example(CompleteField.class)
public class ComplexNumbers extends CompleteComplexNumbers<ComplexNumber, RealNumber, RealField> {

    public static final ComplexNumbers INSTANCE = new ComplexNumbers();

    private ComplexNumbers() {
        super(ComplexNumber.class, RealField.INSTANCE);
    }

    @Override
    ComplexNumber of(RealNumber real, RealNumber imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public String toString() {
        return "ℂ" + TextUtils.subscript("p");
    }

    @Override
    RealNumber atan2(RealNumber imaginary, RealNumber real) {
        return RealField.INSTANCE.atan2(imaginary, real);
    }
}
