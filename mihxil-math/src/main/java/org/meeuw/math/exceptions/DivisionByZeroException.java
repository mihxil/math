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
 * Division by zero is mostly impossible. It's like multiplying by the reciprocal of {@link org.meeuw.math.abstractalgebra.AdditiveGroup#zero}
 * @author Michiel Meeuwissen
 */
public class DivisionByZeroException extends ReciprocalException {

    public DivisionByZeroException(String s) {
        super(s);
    }

    public DivisionByZeroException(Throwable cause) {
        super(cause.getMessage());
        initCause(cause);
    }

    public DivisionByZeroException(Object e, Object divisor) {
        super("Division by zero exception: " + e + "/" + divisor);
    }

    public DivisionByZeroException(Object e, Object divisor, Throwable cause) {
        super("Division exception: " + e + "/" + divisor + " "  + cause.getMessage());
        initCause(cause);
    }
}
