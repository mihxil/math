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
package org.meeuw.math.numbers;

/**
 * Sizeable objects have {@link #abs()}
 *
 * TODO is the difference between {@link org.meeuw.math.abstractalgebra.MetricSpaceElement} and Sizeable Meaningful?
 * are there objects which are one, but not the other?
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 *
 */
public interface Sizeable<E extends Scalar<E>> {

    E abs();
}
