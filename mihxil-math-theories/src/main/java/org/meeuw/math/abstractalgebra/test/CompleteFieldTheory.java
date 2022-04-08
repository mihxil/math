package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.CompleteField;
import org.meeuw.math.abstractalgebra.CompleteFieldElement;
import org.meeuw.math.exceptions.IllegalLogException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.POWER;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface CompleteFieldTheory<E extends CompleteFieldElement<E>> extends
    FieldTheory<E> {

    @Property
    default void getUnary(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThat(struct.getSupportedUnaryOperators()).contains(SQRT, SIN, COS);
    }

    @Property
    default void getOperators(@ForAll(STRUCTURE) CompleteField<E> struct) {
        assertThat(struct.getSupportedOperators()).contains(POWER);
    }

    @Property
    default void lnAndPow(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        try {
            E expectedPow = a.ln().times(b).exp();
            E pow = a.pow(b);
            assertThat(expectedPow.eq(pow))
                .withFailMessage(POWER.stringify(a, b) + " = " + pow + " ≠ " + expectedPow
                ).isTrue();
        } catch (IllegalLogException illegalLogException){
            Assume.that(!LN.isAlgebraicFor(a));
            getLogger().warn(illegalLogException.getMessage());
        }

    }

}
