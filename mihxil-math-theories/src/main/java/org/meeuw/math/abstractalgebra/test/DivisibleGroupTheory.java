package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.DivisibleGroupElement;
import org.meeuw.math.exceptions.DivisionByZeroException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisibleGroupTheory<E extends DivisibleGroupElement<E>>
    extends
    MultiplicativeAbelianGroupTheory<E> {

    @Property
    default void dividedByLong(@ForAll(ELEMENTS) E v1, @ForAll("positiveLongs") long divisor) {
        try {
            assertThat(v1.dividedBy(divisor).getStructure()).isEqualTo(v1.getStructure());
            assertThat(v1.dividedBy(divisor).times(divisor)).isEqualTo(v1);
        } catch (DivisionByZeroException divisionByZeroException) {
            getLogger().info("{} / {} -> {}", v1, divisor, divisionByZeroException.getMessage());
            Assume.that(false);
        }
    }

    @Provide
    default Arbitrary<Long> positiveLongs() {
        return Arbitraries.longs().between(1, 1_000_000);
    }

}
