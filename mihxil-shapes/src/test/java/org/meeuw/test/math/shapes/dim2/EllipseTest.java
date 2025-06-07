package org.meeuw.test.math.shapes.dim2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.assertj.Assertions;
import org.meeuw.math.shapes.dim2.Ellipse;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.theories.BasicObjectTheory;

import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

class EllipseTest implements BasicObjectTheory<Ellipse<UncertainReal>> {


    @Override
    public Arbitrary<@NonNull Ellipse<UncertainReal>> datapoints() {
        return Arbitraries.doubles().ofScale(3)
            .between(0.001, 1000)
            .flatMap(radiusx -> Arbitraries.doubles().ofScale(3)
                .between(0.001, 1000)
                .map(radiusy -> new Ellipse<>(element(radiusx), element(radiusy))));
    }

    @Test
    public void perimeter1() {
        Ellipse<UncertainReal> ellipse = new Ellipse<>(element(1.0), element(1.0));
        Assertions.assertThatAlgebraically(ellipse.perimeter()).isEqTo(element(2 * Math.PI)); // approximation, not exact
    }
    @Test
    public void perimeter2() {
        Ellipse<UncertainReal> ellipse = new Ellipse<>(element(10.0), element(8.0));
        Assertions.assertThatAlgebraically(ellipse.perimeter()).isEqTo(element(
            56.72333577794859 // https://circumferencecalculator.net/ellipse-perimeter-calculator
            )
        ); // approximation, not exact
    }
        {
            Ellipse<UncertainReal> ellipse = new Ellipse<>(element(1.0), element(1.0));

    }

}
