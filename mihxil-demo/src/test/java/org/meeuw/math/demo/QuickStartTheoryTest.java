package org.meeuw.math.demo;

import lombok.extern.java.Log;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.theories.ComparableTheory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
public class QuickStartTheoryTest implements ComparableTheory<MyComparable>{


    @Override
    @Provide
    public Arbitrary<MyComparable> datapoints() {
        return Arbitraries.integers().between(-10, 10).map(MyComparable::new);
    }

    /**
     * Other (junit) tests can just be added in the same class too.
     */
    @Test
    public void otherTest() {
        assertEquals("MyComparable{value=1}", new MyComparable(1).toString());
    }
}
