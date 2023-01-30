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
package org.meeuw.math.uncertainnumbers;


/**
 * Marker interface that this object is not exact.
 * <p>
 * It should have two methods {@code getValue} and {@code getUncertainty}. The returns types may be primitive, so are not in this interface.
 *
 * @see UncertainDouble Where these methods return <pre>double</pre>
 * @see UncertainNumber Where these methods return some <pre>Number</pre>
 * @since 0.7
 */
public interface Uncertain {

    boolean isExact();

    /**
     * Explicitly strict equals. The values must be equal exactly, without consideration of the uncertainty.
     */
    boolean strictlyEquals(Object o);

}
