package org.meeuw.test.math.operators;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NoSuchOperatorException;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class BasicAlgebraicBinaryOperatorTest {

    static class El implements AdditiveGroupElement<El> {

        @Override
        public Struct getStructure() {
            return new Struct();
        }

        @Override
        public El plus(El summand) {
            return this;
        }

        @Override
        public El negation() {
            return null;
        }


    }
    static class Struct implements AdditiveGroup<El> {

        @Override
        public Cardinality getCardinality() {
            return new Cardinality(1);
        }

        @Override
        public Class<El> getElementClass() {
            return El.class;
        }

        @Override
        public El zero() {
            return new El();
        }
    }

    @Test
    public void powInverse() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.POWER.inverse(new El());
        }).isInstanceOf(NoSuchOperatorException.class);

    }

    @Test
    public void addInverse() {
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.inverse(new El());
        }).isInstanceOf(IllegalStateException.class);

    }
}
