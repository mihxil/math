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
package org.meeuw.math.abstractalgebra.complex;

import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.Streamable;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.streams.StreamUtils;

/**
 * The {@link Field} of {@link GaussianRational Gaussian Rationals}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @see GaussianRational
 */
@Example(Field.class)
@Singleton
public class GaussianRationals extends AbstractComplexNumbers<GaussianRational, RationalNumber, RationalNumbers>
    implements Field<GaussianRational>, Streamable<GaussianRational> {

    public static final GaussianRationals INSTANCE = new GaussianRationals();

    private GaussianRationals() {
        super(GaussianRational.class, RationalNumbers.INSTANCE);
    }

    @Override
    public Stream<GaussianRational> stream() {
        return StreamUtils.diagonalStream(
            RationalNumbers.INSTANCE::reverseStream,
            RationalNumbers.INSTANCE::stream,
            this::of
        );
    }

    @Override
    GaussianRational of(RationalNumber real, RationalNumber imaginary) {
        return new GaussianRational(real, imaginary);
    }

    @Override
    public String toString() {
        return "\uD835\uDC10(i)";
    }

}
