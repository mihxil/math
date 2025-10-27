package org.meeuw.test.math.abstractalgebra.polynomial;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.polynomial.Polynomial;
import org.meeuw.theories.abstractalgebra.RingTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.integers.Integers.INSTANCE;
import static org.meeuw.math.abstractalgebra.polynomial.PolynomialRing.INTEGER_POLYNOMIALS;

public class PolynomialTest implements RingTheory<Polynomial<IntegerElement>> {



    @Override
    public Arbitrary<Polynomial<IntegerElement>> elements() {
        return Arbitraries.randomValue(INTEGER_POLYNOMIALS::nextRandom);
    }

    @Test
    public void basicTest() {
        Polynomial<IntegerElement> fivex = INTEGER_POLYNOMIALS.newElement(INSTANCE.newElement(0), INSTANCE.newElement(5));
        assertThat(fivex.toString()).isEqualTo("5·x");

        assertThat(fivex.plus(fivex).toString())
            .isEqualTo("10·x");
        assertThat(
            fivex.times(fivex)
                .plus(fivex)
                .plus(INTEGER_POLYNOMIALS.one()).toString())
            .isEqualTo("1 + 5·x + 25·x²");

    }

    @Test
    public void derivativeTest() {
        Polynomial<IntegerElement> fivex = INTEGER_POLYNOMIALS.newElement(
            INSTANCE.newElement(0),
            INSTANCE.newElement(5)
        );

        Polynomial<IntegerElement> longer = fivex.times(fivex)
                .plus(fivex)
                .plus(INTEGER_POLYNOMIALS.one());

        var derived = longer.derivative();
        assertThat(derived.toString()).isEqualTo("5 + 50·x");
    }

    @Test
    public void fromStringTestAscii() {
         Polynomial<IntegerElement> pol = INTEGER_POLYNOMIALS.fromString("1 - 60x + 7x^2");
         assertThat(pol.toString()).isEqualTo("1 - 60·x + 7·x²");
     }

    @Test
    public void fromStringX() {
         Polynomial<IntegerElement> pol = INTEGER_POLYNOMIALS.fromString("x");
         assertThat(pol.toString()).isEqualTo("x");


         Polynomial<IntegerElement> pol2 = INTEGER_POLYNOMIALS.fromString("-x");
         assertThat(pol2.toString()).isEqualTo("-x");
     }

    @Test
    public void fromString0() {
         Polynomial<IntegerElement> pol = INTEGER_POLYNOMIALS.fromString("0");
         assertThat(pol.toString()).isEqualTo("0");
         assertThat(pol).isEqualTo(INTEGER_POLYNOMIALS.zero());

     }


    @Test
    public void fromStringTest() {
        Polynomial<IntegerElement> pol = INTEGER_POLYNOMIALS.fromString("7·x² + 1 - 60·x + 2·x²");
        assertThat(pol.toString()).isEqualTo("1 - 60·x + 9·x²");

    }


}
