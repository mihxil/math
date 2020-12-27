package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.assertj.core.api.Assumptions;
import org.meeuw.math.abstractalgebra.AdditiveMonoidElement;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.SignedNumber;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface SignedNumberTheory<E extends SignedNumber & Scalar<E>> extends NumberTheory<E> {

    @Property
    default void signumOfZero(@ForAll(ELEMENT) E e) {
        Assumptions.assumeThat(e).isInstanceOf(AdditiveMonoidElement.class);
        assertThat(((SignedNumber) ((AdditiveMonoidElement<?>) e).getStructure().zero()).signum()).isEqualTo(0);
    }

    @Property
    default void compareToConsistentWithSignum(@ForAll(ELEMENTS) E e) {
        Assumptions.assumeThat(e).isInstanceOf(AdditiveMonoidElement.class);
        E zero = (E) ((AdditiveMonoidElement<?>) e).getStructure().zero();
        int compareToZero = e.compareTo(zero);

        if (compareToZero == 0) {
            assertThat(e.signum()).isEqualTo(0);
            assertThat(e).isEqualTo(zero);
        } else if (compareToZero < 0) {
            assertThat(e.signum()).isEqualTo(-1);
        } else {
            assertThat(e.signum()).isEqualTo(1);
        }
    }

}
