package org.meeuw.test.math.shapes.dim2;

import lombok.extern.java.Log;

import net.jqwik.api.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim2.Circle;

import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.math.abstractalgebra.reals.RealField.element;

@Log
public class CircleTest {

    @Nested
    @Group
    public  class RationalCircleTest implements ShapeTheory<RationalNumber, BigDecimalElement, Circle<RationalNumber, BigDecimalElement>> {

        @Override
        public Arbitrary<@NonNull Circle<RationalNumber, BigDecimalElement>> datapoints() {
            return Arbitraries
                .randomValue(INSTANCE::nextRandom)
                .map(Circle::new);
        }
    }

    @Nested
    @Group
    public class RealCircleTest implements ShapeTheory<RealNumber, RealNumber, Circle<RealNumber, RealNumber>> {

        public static Circle<RealNumber, RealNumber> circle = new Circle<>(exactly(1)).times(exactly(2));

        @Test
        public void area() {
            assertThatAlgebraically(circle.area())
                .isEqTo(exactly(4 * Math.PI));
        }

        @Test
        public void perimeter() {
            assertThatAlgebraically(circle.perimeter())
                .isEqTo(exactly(4 * Math.PI));
        }

        @Test
        public void diameter() {
            assertThatAlgebraically(circle.diameter())
                .isEqTo(circle.radius().times(2));
        }

        @Override
        public Arbitrary<@NonNull Circle<RealNumber, RealNumber>> datapoints() {
            return Arbitraries.doubles().ofScale(3).between(0.001, 1000)
                .map(d -> new Circle<>(element(d)));
        }

    }
}
