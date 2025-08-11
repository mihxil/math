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
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.CompleteField;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;

/**
 * The {@link Field} of {@link ComplexNumber}s, backed by {@link BigDecimalElement}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Log
@Example(CompleteField.class)
@Singleton
public class BigComplexNumbers extends CompleteComplexNumbers<BigComplexNumber, BigDecimalElement, BigDecimalField> {

    /**
     * This is the singleton instance of this class.
     */
    public static final BigComplexNumbers INSTANCE = new BigComplexNumbers();

    private BigComplexNumbers() {
        super(BigComplexNumber.class, BigDecimalField.INSTANCE);
    }

    @Override
    BigComplexNumber of(BigDecimalElement real, BigDecimalElement imaginary) {
        return new BigComplexNumber(real, imaginary);
    }

    @Override
    public String toString() {
        return "ℂ";
    }

    @Override
    BigDecimalElement atan2(BigDecimalElement imaginary, BigDecimalElement real) {
        return BigDecimalField.INSTANCE.atan2(imaginary, real);
    }

}
