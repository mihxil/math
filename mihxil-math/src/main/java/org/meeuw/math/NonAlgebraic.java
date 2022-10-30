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
 * Marker for operator methods, to indicate that they are supported but non algebraically. I.e. they may not always be
 * allowed (e.g. you cannot add any two physical numbers) or they result an object not from the same algebra (e.g. the absolute value from the algebra of 'negative numbers' can be taken, but is not an negative number.
 *
 * @since 0.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.METHOD})
public @interface NonAlgebraic {

    String value() default "#default";
}
