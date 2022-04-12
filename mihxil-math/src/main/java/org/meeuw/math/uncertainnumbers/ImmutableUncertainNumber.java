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
package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.util.function.Supplier;

import static org.meeuw.math.CollectionUtils.memoize;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber<N extends Number> implements UncertainNumber<N> {

    @Getter
    private final N value;

    private final Supplier<N> uncertainty;

    public static <N extends Number> ImmutableUncertainNumber<N> of(N value, Supplier<N> uncertainty) {
        return new ImmutableUncertainNumber<>(value, uncertainty);
    }

    public ImmutableUncertainNumber(N value, N uncertainty) {
        this.value = value;
        this.uncertainty = () -> uncertainty;
    }

    public ImmutableUncertainNumber(N value, Supplier<N> uncertainty) {
        this.value = value;
        this.uncertainty = memoize(uncertainty);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return equals(o, 1);
    }

    @Override
    public int hashCode() {
        // must return constant to ensure that this is consistent with equals
        return 0;
    }

    @Override
    public N getUncertainty() {
        return this.uncertainty.get();
    }
}
