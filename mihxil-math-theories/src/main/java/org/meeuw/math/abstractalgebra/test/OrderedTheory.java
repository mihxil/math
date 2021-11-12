package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.test.AlgebraicStructureTheory.STRUCTURE;

/**
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface OrderedTheory<E extends Ordered<E> & AlgebraicElement<E>> extends ElementTheory<E> {

    @Property
    default void getComparisonOperators(@ForAll(STRUCTURE) AbstractAlgebraicStructure<E> struct) {
        assertThat(struct.getSupportedComparisonOperators()).contains(ComparisonOperator.LT, ComparisonOperator.LTE, ComparisonOperator.GT, ComparisonOperator.GTE);
    }

    @Property
    default void orderedReflexive(@ForAll(ELEMENTS) E e) {
        assertThat(e.lte(e)).isTrue();
    }

    @Property
    default void orederedTransitive(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b, @ForAll(ELEMENTS) E c) {
        if (a.lte(b)) {
            // a <= b
            if (b.lte(c)) {
                // a <= b, b <= c ->  a <= c
                assertThat(a.lte(c)).withFailMessage("{} <= {} and {} <= {} -> {} <= {}", a, b, b, c, a, c).isTrue();
            } else {
                getLogger().debug("a <= b,  c <=  b");
            }
        } else {
            // b < a
            if (a.lte(c)) {
                // b < a, a < c
                assertThat(b.lt(c)).withFailMessage("{} < {} and {} < {} -> {} < {}", b, a, a, c, b, c).isTrue();
            } else {
                // b < a, a > c
                getLogger().debug("b > a,  a < c");
            }
        }
    }

    @Property
    default void orderedAntisymmetric(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        if (a.lte(b) && b.lte(a)) {
            assertThat(a.eq(b)).isTrue();
        }
    }

    @Property
    default void orderedStronglyConnected(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat(a.lte(b) || b.lte(a)).isTrue();
    }
}
