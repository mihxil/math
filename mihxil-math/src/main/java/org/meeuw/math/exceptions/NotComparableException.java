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
 * <p>In some structures some elements can be compared, but others can't, in which case the elements can be {@link Comparable}, {@link org.meeuw.math.abstractalgebra.StrictlyOrdered}, but may throw this exception.
 * </p>
 * <p>E.g. physical numbers can be compared only if they have the same dimensions. Otherwise it simply does not make sense.
 * </p>
 *
 */
public class NotComparableException extends IllegalArgumentException {

    public NotComparableException(String s) {
        super(s);
    }
}
