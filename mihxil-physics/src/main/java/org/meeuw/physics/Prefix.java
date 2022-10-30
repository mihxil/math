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
package org.meeuw.physics;

import java.util.Optional;
import java.util.function.DoubleSupplier;

/**
 * Multiplication prefix that can be used on a {@link Unit}, to get a new {@code code} that differs a certain factor only, but has the same {@link DimensionalAnalysis}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Prefix extends DoubleSupplier {

    Optional<? extends Prefix> times(Prefix prefix);

    Optional<? extends Prefix> dividedBy(Prefix prefix);

    Optional<? extends Prefix> reciprocal();

    Optional<? extends Prefix> inc();

    Optional<? extends Prefix> dec();

    @Override
    String toString();

}
