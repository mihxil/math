package org.meeuw.test.math.shapes.dim2;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.shapes.dim2.Circle;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.math.abstractalgebra.reals.RealField.element;

@Log4j2
public class CircleTest {

    @Nested
    @Group
    public  class RationalCircleTest implements ShapeTheory<RationalNumber, Circle<RationalNumber>> {

        @Override
        public Arbitrary<@NonNull Circle<RationalNumber>> datapoints() {
            return Arbitraries
                .randomValue(INSTANCE::nextRandom)
                .map(Circle::new);
        }

    }

    @Nested
    @Group
    public class RealCircleTest implements ShapeTheory<RealNumber, Circle<RealNumber>> {

        public static Circle<RealNumber> circle = new Circle<>(exactly(1)).times(exactly(2));

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
        public Arbitrary<@NonNull Circle<RealNumber>> datapoints() {
            return Arbitraries.doubles().ofScale(3).between(0.001, 1000)
                .map(d -> new Circle<>(element(d)));
        }

    }
}
