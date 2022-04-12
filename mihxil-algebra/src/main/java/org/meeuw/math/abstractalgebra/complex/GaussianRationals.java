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
package org.meeuw.math.abstractalgebra.complex;

import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

/**
 * The {@link Field} of {@link GaussianRational}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(Field.class)
public class GaussianRationals extends AbstractComplexNumbers<GaussianRational, RationalNumber, RationalNumbers>
    implements Field<GaussianRational>, Streamable<GaussianRational> {

    public static final GaussianRationals INSTANCE = new GaussianRationals();

    GaussianRationals() {
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
