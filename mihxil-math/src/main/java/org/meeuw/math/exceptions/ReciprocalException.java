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

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * Represents a problem with taking the reciprocal of an element. The must well known extension would be {@link DivisionByZeroException}.
 * But it is also thrown by {@link MultiplicativeGroupElement#reciprocal()} if this is exceptionaly not possible.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ReciprocalException extends InverseException {
    public ReciprocalException(String s) {
        super(s);
    }

    public ReciprocalException(Throwable cause) {
        super(cause);
    }
}
