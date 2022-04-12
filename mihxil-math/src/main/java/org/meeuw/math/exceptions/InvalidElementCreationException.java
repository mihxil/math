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
 * Throws if you try to create an invalid element of some algebra. E.g. a rational number with zero denominator.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class InvalidElementCreationException extends InvalidOperationException {
    public InvalidElementCreationException(String s) {
        super(s);
    }

    public InvalidElementCreationException(NotASquareException notASquareException) {
        super(notASquareException.getMessage());
        initCause(notASquareException);
    }
}
