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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import org.meeuw.math.numbers.SizeableScalar;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Marks an argument as that it needs to be a square.
 *
 * This applies to {@link Number#longValue()}, to {@link SizeableScalar#longValue()}
 * but also, to <em>arrays</em>. For a one dimensional array the length must be a square (supposing that it actually represents a 2 dimension square matrix. For a two dimension matrix it is checked whether it is properly square, i.e all rows and columns have the same size.
 *
 */
@Target({METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = SquareValidator.class)
@Documented
public @interface Square {

    String message() default "{org.meeuw.math.notASquare}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value() default -1;

    boolean invertible() default false;

}
