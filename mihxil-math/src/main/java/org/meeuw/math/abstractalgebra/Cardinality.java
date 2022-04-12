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
package org.meeuw.math.abstractalgebra;

import jakarta.validation.constraints.Positive;

import java.math.BigInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.exceptions.CardinalityException;
import org.meeuw.math.text.TextUtils;


/**
 * Represents the concept of 'Cardinality' of an {@link AlgebraicStructure}. i.e. the number of elements,
 * but it also supports the 'infinite' values {@link #ALEPH_0} and {@link #ALEPH_1}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Cardinality implements Comparable<Cardinality>, MultiplicativeSemiGroupElement<Cardinality> {

    private final BigInteger value;

    public static final Cardinality ONE = new Cardinality(1);


    /**
     * The cardinality of natural numbers. 'Countably infinite'
     */
    public static final Cardinality ALEPH_0 = new Cardinality(-1){
        @Override
        public String toString() {
            // aleph left to right
            return "\u05D0\u200E" + TextUtils.subscript(0);
        }
    };

    /**
     * The cardinality of the set of all countable ordinal numbers. Itself 'uncountably infinite'.
     */
    public static final Cardinality ALEPH_1 = new Cardinality(-2) {
        @Override
        public String toString() {
            //  aleph left to right
            return "\u05D0\u200E" + TextUtils.subscript(1);
        }
    };

    /**
     * The cardinality of the continuum. According to the continuum hypothesis this equals {@link #ALEPH_1}
     */
    public static final Cardinality C = new Cardinality(-3) {
        @Override
        public String toString() {
            return "\uD835\uDCEC\t\uD835\uDD20";
        }
    };

    public static Cardinality of(@Positive BigInteger value) {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        return new Cardinality(value);
    }

    public static Cardinality of(@Positive long value) {
        return of(BigInteger.valueOf(value));
    }


    private Cardinality(BigInteger value) {
        this.value = value;
    }
    private Cardinality(long value) {
        this.value = BigInteger.valueOf(value);
    }
    private static final class Structure implements MultiplicativeSemiGroup<Cardinality>, Streamable<Cardinality> {
        final static Structure INSTANCE = new Structure();
        @Override
        public Cardinality getCardinality() {
            return Cardinality.ALEPH_0;
        }

        @Override
        public Class<Cardinality> getElementClass() {
            return Cardinality.class;
        }
        @Override
        public Stream<Cardinality> stream() {
            return
                Stream.concat(
                Stream.of(Cardinality.ALEPH_0, Cardinality.ALEPH_1, Cardinality.C),
                    IntStream.iterate(1, i -> i + 1).mapToObj(Cardinality::new)
                );
        }
    }

    @Override
    public MultiplicativeSemiGroup<Cardinality> getStructure() {
        return Structure.INSTANCE;
    }

    @Override
    public Cardinality times(Cardinality cardinality) {
        if (value.signum() > 0 && cardinality.value.signum() > 0) {
            return new Cardinality(value.multiply(cardinality.value));
        } else {
            if (value.compareTo(cardinality.value) < 0) {
                return this;
            } else {
                return cardinality;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cardinality that = (Cardinality) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public BigInteger getValue() {
        if (value.signum() >= 0) {
            return value;
        } else {
            throw new CardinalityException(this + " has no finite integer value");
        }
    }

    public boolean isInfinite() {
        return value.signum() < 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Cardinality o) {
        if (o.isInfinite()) {
            if (! isInfinite()) {
                return -1;
            } else {
                return o.value.compareTo(value);
            }
        } else {
            if (isInfinite()) {
                return 1;
            } else {
                return value.compareTo(o.value);
            }
        }
    }


}
