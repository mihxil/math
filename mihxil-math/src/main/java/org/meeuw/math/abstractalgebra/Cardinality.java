package org.meeuw.math.abstractalgebra;

import lombok.Getter;

import org.meeuw.math.text.TextUtils;

/**
 * Represents the concept of 'Cardinality' of an {@link AlgebraicStructure}. i.e. the number of elements,
 * but it also supports the 'infinite' values {@link #ALEPH_0} and {@link #ALEPH_1}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Cardinality implements Comparable<Cardinality> {

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


    public Cardinality(long value) {
        this.value = value;
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
