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
package org.meeuw.math.exceptions;

/**
 * This can be thrown when converting a value and the original value is not finite, but the target type requires a finite value. E.g. 'padic' numbers, can sometimes be seen as 'infinite' integers, and converting them to integers may result this exception.
 * <p>
 * It can also be throws for other cases, e.g. when trying an operation on an infinite structure, which is not possible then (e.g. generating
 * a Cayley table for an infinite group).
 */
public class NotFiniteException extends IllegalArgumentException {

    public NotFiniteException(String s) {
        super(s);
    }
}
