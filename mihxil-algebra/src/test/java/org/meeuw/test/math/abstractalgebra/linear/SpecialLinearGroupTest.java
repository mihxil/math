package org.meeuw.test.math.abstractalgebra.linear;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.linear.SpecialLinearGroup;
import org.meeuw.math.abstractalgebra.linear.SpecialLinearMatrix;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class SpecialLinearGroupTest {

    @Test
    void of() {
        SpecialLinearMatrix<IntegerElement> e = SpecialLinearMatrix.of(
            IntegerElement.of(1), IntegerElement.of(2),
            IntegerElement.of(0), IntegerElement.of(-1)
        );
        log.info("{}", e);
        assertThat(e.getStructure().getDimension()).isEqualTo(2);

        IntegerElement det = e.determinant();
        assertThat(det.getValue()).isEqualTo(-1);

        assertThat(e.getStructure().toString()).isEqualTo("SL₂(ℤ)");
    }

    //@Test
    public void stream() {
        SpecialLinearGroup<IntegerElement> e = SpecialLinearGroup.of(3, Integers.INSTANCE);
        e.stream().limit(100).forEach(m -> {
            assertThat(m.determinant()).isIn(IntegerElement.ONE, IntegerElement.ONE.negation());
        });

    }

    public static class IntegersTest implements
        MultiplicativeGroupTheory<SpecialLinearMatrix<IntegerElement>> {


        @Property
        public void determinant(@ForAll(ELEMENTS) SpecialLinearMatrix<IntegerElement> matrix) {
            log.info("det({}) = {}", matrix, matrix.determinant());
            assertThat(matrix.determinant()).isIn(IntegerElement.ONE, IntegerElement.ONE.negation());

        }
        @Override
        public Arbitrary<SpecialLinearMatrix<IntegerElement>> elements() {
            SpecialLinearGroup<IntegerElement> e = SpecialLinearGroup.of(3, Integers.INSTANCE);
            return Arbitraries.randomValue(e::nextRandom);
        }
    }

}
