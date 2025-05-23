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
 * Gets thrown if trying to take an impossible logarithm.
 * @since 0.9
 */
public class IllegalLogarithmException extends OperationException {

    public IllegalLogarithmException(ArithmeticException s, String operationAsString) {
        super(s, operationAsString);
    }

    public IllegalLogarithmException(String message, String operationAsString) {
        super(message, operationAsString);
    }
}
