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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Marks an argument as that it needs to be a prime.
 */
@Target({METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = PrimeValidator.class)
@Documented
public @interface Prime {

    String message() default "{org.meeuw.math.notAPrime}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * If set to true, will check wether the value is a power of a prime, rather then
     * prime.
     */
    boolean power() default false;


}
