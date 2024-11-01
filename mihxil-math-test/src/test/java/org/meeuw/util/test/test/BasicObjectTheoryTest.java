package org.meeuw.util.test.test;


import lombok.Data;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.theories.ComparableTheory;

public class BasicObjectTheoryTest implements ComparableTheory<BasicObjectTheoryTest.A> {


    @Data
    public static class A implements Comparable<A> {
        final int value;


        public A(int v) {
            this.value = v;
            // System.out.println("Created " + this);
        }
        @Override
        public String toString() {
            return value + " " + super.hashCode();
        }

        @Override
        public int compareTo(A o) {
            return Integer.compare(value, o.value);
        }
    }

    @Override
    public Arbitrary<A> datapoints() {
        return Arbitraries.integers().map(A::new);
    }

}
