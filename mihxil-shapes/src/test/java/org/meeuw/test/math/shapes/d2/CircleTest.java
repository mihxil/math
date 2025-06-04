package org.meeuw.test.math.shapes.d2;

import org.junit.jupiter.api.Test;

import org.meeuw.math.shapes.d2.Circle;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

public class CircleTest {

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

}
