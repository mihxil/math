package org.meeuw.test.math.abstractalgebra.circlegroup;

import lombok.extern.java.Log;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.jupiter.WithNumberConfiguration;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.circlegroup.CircleGroup;
import org.meeuw.math.abstractalgebra.circlegroup.CircleElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.theories.abstractalgebra.AdditiveGroupTheory;

@Log
@WithNumberConfiguration
public class CircleGroupTest {




    static class AngleTest implements AdditiveGroupTheory<CircleElement<RationalNumber, BigDecimalElement>> {

        CircleGroup<RationalNumber,  BigDecimalElement> group = CircleGroup.of(RationalNumber.of(360));

        @Override
        public Arbitrary<CircleElement<RationalNumber, BigDecimalElement>> elements() {
            return
                Arbitraries.randomValue(
                    group::nextRandom
                    )
                    .injectDuplicates(0.1)
                    .dontShrink();


        }
    }
}
