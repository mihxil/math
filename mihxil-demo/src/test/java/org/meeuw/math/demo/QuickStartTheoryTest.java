package org.meeuw.math.demo;

import lombok.extern.java.Log;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.theories.ComparableTheory;

@Log
public class QuickStartTheoryTest implements ComparableTheory<MyComparable>{


    @Override
    public Arbitrary<MyComparable> datapoints() {
        return Arbitraries.integers().between(-10, 10).map(MyComparable::new);
    }
}
