package org.meeuw.test.math.abstractalgebra;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.exceptions.InvalidAlgebraicResult;
import org.meeuw.math.exceptions.NoSuchOperatorException;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.test.math.sample.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class OperatorTest {
    static {
        Assertions.setMaxStackTraceElementsDisplayed(10);
    }

    @Test
    public void add() {
        Assertions.assertThat(BasicAlgebraicBinaryOperator.ADDITION.apply(new A(), new A())).isInstanceOf(A.class);
    }

    @Test
    public void stringify() {
        assertThat(BasicAlgebraicBinaryOperator.ADDITION.stringify(new A(), new A())).isEqualTo("<a> + <a>");
        assertThat(BasicAlgebraicBinaryOperator.ADDITION.getStringify().apply("x", "y")).isEqualTo("x + y");
    }

    @Test
    public void invalidResult() {
        assertThatThrownBy(() ->
            BasicAlgebraicBinaryOperator.ADDITION.apply(new ANull(), new A())
        ).isInstanceOf(InvalidAlgebraicResult.class);
    }

    @Test
    public void myException() {
        assertThatThrownBy(() ->
            BasicAlgebraicBinaryOperator.ADDITION.apply(new AIllegalArgument(), new A())
        ).isInstanceOf(MyException.class).hasMessage("foo bar");
    }

    @Test
    public void wrongArgument() {
        assertThatThrownBy(() ->
            BasicAlgebraicBinaryOperator.MULTIPLICATION.apply(new A(), new A())
        ).isInstanceOf(NoSuchOperatorException.class).hasMessage("A <a> has no operator 'times'");
    }

    @Test
    public void illegalArgument() {
        assertThatThrownBy(() ->
            BasicAlgebraicBinaryOperator.DIVISION.apply(new A(), new A())
        ).isInstanceOf(NoSuchOperatorException.class).hasMessage("A <a> has no operator 'dividedBy'");
    }

    @Test
    public void inverse() {
        assertThatThrownBy(() ->
            BasicAlgebraicBinaryOperator.ADDITION.inverse(new A())
        ).isInstanceOf(NoSuchOperatorException.class);
    }

    @Test
    public void unity() {
        assertThatThrownBy(() ->
            BasicAlgebraicBinaryOperator.ADDITION.unity(new AStruct())
        ).isInstanceOf(NoSuchOperatorException.class);
    }

}
