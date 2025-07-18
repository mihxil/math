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
package org.meeuw.math.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.numbers.SignedNumber;

/**
 * Implementation for {@link NotZero}
 */
public class NotZeroValidator implements ConstraintValidator<NotZero, Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value instanceof ScalarFieldElement<?> number) {
            return ! number.isZero();
        }
        if (value instanceof Number number) {
            return number.doubleValue() != 0;
        }

        if (value instanceof SignedNumber<?> number) {
            return ! number.isZero();
        }
        return true; // we don't know, so assume it is valid, it might be null
    }

}
