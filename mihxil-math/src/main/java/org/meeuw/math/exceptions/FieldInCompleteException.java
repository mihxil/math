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
 * Thrown if trying to perform an operation on an element, that is impossible because some underlying implementation is not complete. E.g. for most operations on {@link org.meeuw.math.abstractalgebra.Vector} it is not essential that the elements of the vector are complete, but implementation may also implement e.g.  {@link org.meeuw.math.numbers.Sizeable}, requiring that you can take the square root, which is only possible if the element are {@link org.meeuw.math.abstractalgebra.CompleteFieldElement}.
 * @author Michiel Meeuwissen
 */
public class FieldInCompleteException extends UnsupportedMathOperationException {
    public FieldInCompleteException(String s) {
        super(s);
    }
}
