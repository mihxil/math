package org.meeuw.test.math.shapes.dim2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.assertj.Assertions;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.shapes.dim2.Ellipse;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;
import static org.meeuw.math.abstractalgebra.reals.RealField.element;

class EllipseTest  {


    @Nested
    @Group
    public  class RationalEllipseTest implements ShapeTheory<RationalNumber, Ellipse<RationalNumber>> {

        @Override
        public Arbitrary<@NonNull Ellipse<RationalNumber>> datapoints() {
            return Arbitraries
                .randomValue(INSTANCE::nextRandom)
                .tuple2()
                .map((t) -> new Ellipse<>(t.get1(), t.get2()));
        }

    }



    @Nested
    @Group
    public  class RealEllipseTest implements ShapeTheory<RealNumber, Ellipse<RealNumber>> {


        @Override
        public Arbitrary<@NonNull Ellipse<RealNumber>> datapoints() {
            return Arbitraries.doubles().ofScale(3)
                .between(0.001, 1000)
                .flatMap(radiusx -> Arbitraries.doubles().ofScale(3)
                    .between(0.001, 1000)
                    .map(radiusy -> new Ellipse<>(element(radiusx), element(radiusy))));
        }

        @Test
        public void perimeter1() {
            Ellipse<RealNumber> ellipse = new Ellipse<>(element(1.0), element(1.0));
            Assertions.assertThatAlgebraically(ellipse.perimeter()).isEqTo(element(2 * Math.PI)); // approximation, not exact
        }

        @Test
        public void perimeter2() {
            Ellipse<RealNumber> ellipse = new Ellipse<>(element(10.0), element(8.0));
            Assertions.assertThatAlgebraically(ellipse.perimeter()).isEqTo(element(
                    56.72333577794859 // https://circumferencecalculator.net/ellipse-perimeter-calculator
                )
            ); // approximation, not exact
        }

        {
            Ellipse<RealNumber> ellipse = new Ellipse<>(element(1.0), element(1.0));

        }
    }
}
