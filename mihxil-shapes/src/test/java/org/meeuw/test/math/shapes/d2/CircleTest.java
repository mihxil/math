package org.meeuw.test.math.shapes.d2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.shapes.d2.Circle;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.theories.BasicObjectTheory;

import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

public class CircleTest implements BasicObjectTheory<Circle<UncertainReal>> {

    public static Circle<UncertainReal> circle = new Circle<>(exactly(1)).times(exactly(2));

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
    public Arbitrary<@NonNull Circle<UncertainReal>> datapoints() {
        return Arbitraries.doubles().ofScale(3).between(0.001, 1000)
            .map(d -> new Circle<>(element(d)));
    }
}
