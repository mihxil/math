package org.meeuw.test.math.abstractalgebra.polynomial;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.polynomial.Polynomial;
import org.meeuw.math.abstractalgebra.polynomial.PolynomialRing;
import org.meeuw.theories.abstractalgebra.RingTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.integers.Integers.INSTANCE;

public class PolynomialTest implements RingTheory<Polynomial<IntegerElement>> {

    PolynomialRing<IntegerElement> polynomialRing = PolynomialRing.of(INSTANCE);

    @Override
    public Arbitrary<Polynomial<IntegerElement>> elements() {
        return Arbitraries.randomValue(r -> polynomialRing.nextRandom(r));
    }

    @Test
    public void basicTest() {
        Polynomial<IntegerElement> fivex = polynomialRing.newElement(INSTANCE.newElement(0), INSTANCE.newElement(5));
        assertThat(fivex.toString()).isEqualTo("5·x");

        assertThat(fivex.plus(fivex).toString())
            .isEqualTo("10·x");
        assertThat(
            fivex.times(fivex)
                .plus(fivex)
                .plus(polynomialRing.one()).toString())
            .isEqualTo("1 + 5·x + 25·x²");

    }

    @Test
    public void derivativeTest() {
        Polynomial<IntegerElement> fivex = polynomialRing.newElement(
            INSTANCE.newElement(0),
            INSTANCE.newElement(5)
        );
        assertThat(fivex.toString()).isEqualTo("5·x");

        assertThat(fivex.plus(fivex).toString())
            .isEqualTo("10·x");
        assertThat(
            fivex.times(fivex)
                .plus(fivex)
                .plus(polynomialRing.one()).toString())
            .isEqualTo("1 + 5·x + 25·x²");

    }
}
