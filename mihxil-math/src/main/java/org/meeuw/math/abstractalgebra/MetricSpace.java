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

import java.util.function.BiFunction;

import org.meeuw.math.numbers.Scalar;

/**
 * A metric space is a set of element, where each two elements have a {@link MetricSpaceElement#distanceTo(MetricSpaceElement)} between each other.
 * <p>
 * As a 'binary' function this is called the {@link #metric(MetricSpaceElement, MetricSpaceElement) metric} of the metric space    
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> self reference
 * @param <S> the type of the distance
 */
public interface MetricSpace<E extends MetricSpaceElement<E, S>, S extends Scalar<S>> extends BiFunction<E, E, S> {

    E zero();

    /**
     * The 'metric' of this space. Synonymous to {@link #apply(MetricSpaceElement, MetricSpaceElement)}
     */
    default S metric(E e1, E e2) {
        return e1.distanceTo(e2);
    }
    
    @Override
    default S apply(E e1, E e2) {
        return metric(e1, e2);
    }
}
