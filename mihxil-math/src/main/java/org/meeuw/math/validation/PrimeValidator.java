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
package org.meeuw.math.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.meeuw.math.Utils;

public class PrimeValidator implements ConstraintValidator<Prime, Object> {
    boolean power;
    @Override
    public void initialize(Prime constraintAnnotation) {
        power = constraintAnnotation.power();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        long toValidate = SquareValidator.toLong(value);
        if (power) {
            return Utils.isPrimePower(toValidate);
        } else {
            return Utils.isPrime((int) toValidate);
        }

    }
}
