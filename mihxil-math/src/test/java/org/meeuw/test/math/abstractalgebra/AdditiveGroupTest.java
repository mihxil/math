package org.meeuw.test.math.abstractalgebra;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

/**
 * @author Michiel Meeuwissen
 */
class AdditiveGroupTest {

    static class TestElement implements AdditiveGroupElement<TestElement> {

        final static TestElement zero = new TestElement(null);

        private final TestElement negation;

        TestElement(TestElement negation) {
            this.negation = negation == null ? this : negation;
        }

        @Override
        public TestGroup getStructure() {
            return TestGroup.instance;
        }

        @Override
        public TestElement negation() {
            return negation;
        }

        @Override
        public TestElement plus(TestElement summand) {
            return null;
        }
    }

    static class TestGroup implements AdditiveGroup<TestElement> {

        static TestGroup instance = new TestGroup();

        @Override
        public TestElement zero() {
            return TestElement.zero;
        }

        @Override
        public Cardinality getCardinality() {
            return new Cardinality(5);
        }

        @Override
        public Class<TestElement> getElementClass() {
            return TestElement.class;
        }

    }

    @Test
    public void additiveGroupOperators() {
        assertThat(TestGroup.instance.getSupportedOperators())
            .contains(BasicAlgebraicBinaryOperator.ADDITION, BasicAlgebraicBinaryOperator.SUBTRACTION);

    }
    @Test
    public void additiveGroupUnaryOperators() {
        assertThat(TestGroup.instance.getSupportedUnaryOperators())
            .contains(BasicAlgebraicUnaryOperator.NEGATION);
    }

}
