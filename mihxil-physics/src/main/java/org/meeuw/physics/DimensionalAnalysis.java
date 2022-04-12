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
package org.meeuw.physics;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.Streamable;
import org.meeuw.math.text.spi.FormatService;

/**
 * A dimensions object represent a physical dimensional analysis.
 *
 * It is basically a wrapper for an array of exponents, one for each possible {@link Dimension}
 *
 * @author Michiel Meeuwissen
 */
@Log
public class DimensionalAnalysis
    implements
    MultiplicativeGroupElement<DimensionalAnalysis>,
    Streamable<DimensionExponent> {

    @Getter
    final int[] exponents = new int[Dimension.values().length];

    public DimensionalAnalysis(DimensionExponent... dimensions) {
        for (DimensionExponent v : dimensions) {
            exponents[v.getDimension().ordinal()] += v.getExponent();
        }
    }

    public DimensionalAnalysis(int[] exponents) {
        System.arraycopy(exponents, 0, this.exponents, 0, this.exponents.length);
    }

    public static DimensionalAnalysis of(DimensionExponent... dimensions) {
        return new DimensionalAnalysis(dimensions);
    }

    @Override
    public DimensionsGroup getStructure() {
        return DimensionsGroup.INSTANCE;
    }

    @Override
    public DimensionalAnalysis reciprocal() {
        return pow(-1);
    }

    @Override
    public DimensionalAnalysis times(DimensionalAnalysis multiplier) {
        DimensionalAnalysis copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] += multiplier.exponents[i];
        }
        return copy;
    }

    @Override
    public DimensionalAnalysis pow(int exponent) {
        DimensionalAnalysis copy = copy();
        for (int i = 0; i < exponents.length; i++) {
            copy.exponents[i] *= exponent;
        }
        return copy;
    }

    public DimensionalAnalysis copy() {
        return new DimensionalAnalysis(exponents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DimensionalAnalysis units = (DimensionalAnalysis) o;

        return Arrays.equals(exponents, units.exponents);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(exponents);
    }

    @Override
    public String toString() {
        return FormatService.toString(this);
    }

    @Override
    @NonNull
    public Stream<DimensionExponent> stream() {
        return IntStream
            .range(0, Dimension.values().length)
            .mapToObj(i -> DimensionExponent.of(Dimension.values()[i], exponents[i]));
    }


    public DimensionalAnalysis dividedBy(Quantity quantity) {
        return this.dividedBy(quantity.getDimensionalAnalysis());
    }
}
