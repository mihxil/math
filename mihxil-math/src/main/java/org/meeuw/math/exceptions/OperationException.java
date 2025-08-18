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

import lombok.Getter;

/**
 * Indicating that some operation failed. E.g. negatives of sqrt's, inverses of zero. Those kind of things.
 * <p>
 * The operation failed because it received some exceptional argument for which it is not possible.
 * @since 0.9
 */
@Getter
public class OperationException extends MathException {

    final String operationAsString;

    public OperationException(String message, String operationAsString) {
        super(message);
        this.operationAsString = operationAsString;
    }

    public OperationException(ArithmeticException a, String operationAsString) {
        super(a.getMessage());
        initCause(a);
        this.operationAsString = operationAsString;
    }

     public OperationException(String message, String operationAsString, Exception e) {
        super(message);
        initCause(e);
        this.operationAsString = operationAsString;
    }

    @Override
    public String toString() {
        return super.toString() + " for " + operationAsString;
    }

}
