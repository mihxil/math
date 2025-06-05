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
package org.meeuw.math;

import java.lang.annotation.*;

/**
 * Marker for operator methods, to indicate that they are supported but there may be loss of precision.
 * E.g., non {@link org.meeuw.math.uncertainnumbers.Uncertain} elements may still support {@link WithDoubleOperations#times(double)}, and return a new element of the field, but the result may have been subject to rounding.
 *
 * @since 0.16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.METHOD})
public @interface NonExact {

    /**
     * A description for the reason of the non-exactness
     */
    String value() default "#default";


    Strategy strategy() default Strategy.ROUNDING;


    enum Strategy {
        ROUNDING,
        EXCEPTION;
    }


}
