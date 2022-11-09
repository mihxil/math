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
 * Indicating that some operation failed. E.g. negatives of sqrt's, inverses of zero. Those kind of things.
 * <p>
 * The operation failed because it received some exceptional argument for which it is not possible.
 * @since 0.9
 */
public class OperationException extends MathException {

    public OperationException(String s) {
        super(s);
    }

    public OperationException(ArithmeticException a) {
        super(a.getMessage());
        initCause(a);
    }

     public OperationException(String s, Exception e) {
        super(s);
        initCause(e);
    }
}
