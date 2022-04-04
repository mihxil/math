package org.meeuw.math.abstractalgebra;

import lombok.Getter;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.text.TextUtils;


/**
 * Represents the concept of 'Cardinality' of an {@link AlgebraicStructure}. i.e. the number of elements,
 * but it also supports the 'infinite' values {@link #ALEPH_0} and {@link #ALEPH_1}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Cardinality implements Comparable<Cardinality>, MultiplicativeSemiGroupElement<Cardinality> {

    @Getter
    private final long value;

    public static final Cardinality ALEPH_0 = new Cardinality(-1){
        @Override
        public String toString() {
            // aleph left to right
            return "\u05D0\u200E" + TextUtils.subscript(0);
        }
    };
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


    public Cardinality(long value) {
        this.value = value;
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
    };

    @Override
    public MultiplicativeSemiGroup<Cardinality> getStructure() {
        return Structure.INSTANCE;
    }

    @Override
    public Cardinality times(Cardinality cardinality) {
        if (value > 0 && cardinality.value > 0) {
            return new Cardinality(value * cardinality.value);
        } else {
            if (value < cardinality.value) {
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
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Cardinality o) {
        if (o.value < 0) {
            if (value >= 0) {
                return -1;
            } else {
                return Long.compare(o.value, value);
            }
        } else {
            if (value < 0) {
                return 1;
            } else {
                return Long.compare(value, o.value);
            }
        }
    }


}
