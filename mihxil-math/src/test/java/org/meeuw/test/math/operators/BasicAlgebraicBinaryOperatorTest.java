package org.meeuw.test.math.operators;

import java.util.function.BinaryOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidAlgebraicResult;
import org.meeuw.math.exceptions.NoSuchOperatorException;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;


@Execution(SAME_THREAD)
public class BasicAlgebraicBinaryOperatorTest {

    static BinaryOperator<El> p = (a, b) -> a;

    @BeforeEach
    void setup() {
        p = (a, b) -> a;
    }

    static class El implements AdditiveGroupElement<El> {

        @Override
        public Struct getStructure() {
            return new Struct();
        }

        @Override
        public El plus(El summand) {
            return p.apply(this, summand);
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

    @Test
    public void throwsException() {
        p = (a, b) -> { throw new NullPointerException();};
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.apply(new El(), new El());
        }).isInstanceOf(NullPointerException.class);

    }

    @Test
    public void returnsNull() {
        p = (a, b) -> null;
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.ADDITION.apply(new El(), new El());
        }).isInstanceOf(InvalidAlgebraicResult.class);

    }

    @Test
    public void callNonExisting() {
        p = (a, b) -> { throw new NullPointerException();};
        assertThatThrownBy(() -> {
            BasicAlgebraicBinaryOperator.MULTIPLICATION.apply(new El(), new El());
        }).isInstanceOf(NoSuchOperatorException.class);

    }
}
