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
import lombok.With;

import java.util.function.Supplier;

import org.meeuw.math.WithUnits;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

import static org.meeuw.math.CollectionUtils.memoize;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber<N extends Number> implements UncertainNumber<N> , WithUnits  {

    @Getter
    private final N value;

    private final Supplier<N> uncertainty;

    @Getter
    @With
    private final String unitsAsString;

    public static <N extends Number> ImmutableUncertainNumber<N> of(N value, Supplier<N> uncertainty) {
        return new ImmutableUncertainNumber<>(value, uncertainty);
    }

    @lombok.Builder
    private ImmutableUncertainNumber(N value, Supplier<N> uncertainty, String unitsAsString) {
        this.value = value;
        this.uncertainty = uncertainty;
        this.unitsAsString = unitsAsString;
    }

    public ImmutableUncertainNumber(N value, N uncertainty) {
        this(value, () -> uncertainty, null);
    }

    public ImmutableUncertainNumber(N value, Supplier<N> uncertainty) {
        this(value, memoize(uncertainty), null);
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

    @Override
    public String toString() {
        UncertainDoubleFormat formatter = FormatService.getFormat(UncertainDoubleFormatProvider.class);
        return formatter.format(this) + unitsAsString;
    }

}
