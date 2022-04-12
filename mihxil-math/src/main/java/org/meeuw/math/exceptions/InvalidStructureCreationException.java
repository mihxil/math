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
package org.meeuw.math.exceptions;

/**
 * Thrown if a structure is created that is invalid. E.g. a modulo structure with a negative divisor.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class InvalidStructureCreationException extends MathException {
    public InvalidStructureCreationException(String s) {
        super(s);
    }
}
