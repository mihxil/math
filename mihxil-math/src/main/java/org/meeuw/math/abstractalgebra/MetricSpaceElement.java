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
package org.meeuw.math.abstractalgebra;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.Sizeable;

/**
 * Elements of a metric space define a {@link #distanceTo(MetricSpaceElement) distance to} other elements in the same {@link MetricSpace}
 * <p>
 * This is a related to {@link Sizeable}, where the {@link Sizeable#abs() absolute value} is just the distance to {@link MetricSpace#zero()}. There is also a {@link #getStructure() reference} to its {@link MetricSpace structure},
 * very similarly to {@link AbstractAlgebraicElement#getStructure()}.
 * <p>
 * A metric space element can be called a 'point' in the space.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> self reference
 * @param <S> the type of the distance
 */
public interface MetricSpaceElement<E extends MetricSpaceElement<E, S>, S extends Scalar<S>>
    extends Sizeable<S> {

    /**
     * Every point has a reference to the {@link MetricSpace metric space} it belongs to.
     *
     * @see AbstractAlgebraicElement#getStructure()
     */
    MetricSpace<E, S> getStructure();

    /**
     * A {@link MetricSpace} has a notion of a 'distance' between to elements.
     */
    S distanceTo(E otherElement);

    /**
     * {@inheritDoc}
     * <p>
     * For a metric space element this is equivalent to the {@link #distanceTo(MetricSpaceElement) distance to} {@link MetricSpace#zero()}.
     */
    @Override
    @NonAlgebraic
    default S abs() {
        return distanceTo(getStructure().zero());
    }

}
