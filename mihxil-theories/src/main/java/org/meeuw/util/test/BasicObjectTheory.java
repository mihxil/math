package org.meeuw.util.test;


import java.util.*;

import net.jqwik.api.*;

import net.jqwik.api.Tuple.Tuple2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


/**
 * Basic tests on {@link Object#equals(Object)}, {@link Object#hashCode()} and {@link Object#toString()}, which must probably be valid for _any_ override of those.
 *
 * @author Michiel Meeuwissen
 * @since 0.10
 */
public interface BasicObjectTheory<E> {

    String DATAPOINTS = "datapoints";

    String NONNULL_DATAPOINTS = "nonNullDatapoints";

    String EQUAL_DATAPOINTS = "equalDatapoints";

    /**
     * For any non-null reference value x, x.equals(x) should return true
     */
    @SuppressWarnings("EqualsWithItself")
    @Property
    default void equalsIsReflexive(@ForAll(NONNULL_DATAPOINTS) E x) {
        assertThat(x.equals(x)).isTrue();
    }

    /**
     * For any non-null reference values x and y, x.equals(y)
     * should return true if and only if y.equals(x) returns true.
     */
    @Property
    default void equalsIsSymmetric(@ForAll(NONNULL_DATAPOINTS) E x, @ForAll(NONNULL_DATAPOINTS) E y) {
        assertThat(x.equals(y)).isEqualTo(y.equals(x));
    }

    /**
     * For any non-null reference values x, y, and z, if x.equals(y)
     * returns true and y.equals(z) returns true, then x.equals(z)
     * should return true.
     */
    @Property
    default  void equalsIsTransitive(@ForAll(EQUAL_DATAPOINTS) Tuple2<E, E> p1, @ForAll(EQUAL_DATAPOINTS) Tuple2<E, E> p2) {
        assertThat(p1.get1().equals(p2.get2())).isEqualTo(p1.get2().equals(p2.get1()));
    }

    /**
     * For any non-null reference values x and y, multiple invocations
     * of x.equals(y) consistently return true  or consistently return
     * false, provided no information used in equals comparisons on
     * the objects is modified.
     */
    @Property
    default void equalsIsConsistent(@ForAll(NONNULL_DATAPOINTS) E x, @ForAll(DATAPOINTS) E y) {
        boolean alwaysTheSame = x.equals(y);

        for (int i = 0; i < 30; i++) {
            assertThat(x.equals(y)).isEqualTo(alwaysTheSame);
        }
    }

    /**
     * For any non-null reference value x, x.equals(null); should
     * return false.
     */
    @SuppressWarnings("ConstantConditions")
    @Property
    default void equalsReturnFalseOnNull(@ForAll(NONNULL_DATAPOINTS) E x) {
        assertThat(x.equals(null)).isFalse();
    }

    /**
     * For any non-null reference value x, x.equals(new Object); should
     * return false.
     */
    @SuppressWarnings("ConstantConditions")
    @Property
    default void equalsReturnFalseOnOtherObject(@ForAll(NONNULL_DATAPOINTS) E x) {
        assertThat(x.equals(new Object())).isFalse();
    }

    /**
     * Whenever it is invoked on the same object more than once
     * the hashCode() method must consistently return the same
     * integer.
     */
    @Property
    default void hashCodeIsSelfConsistent(@ForAll(NONNULL_DATAPOINTS) E x) {
        int alwaysTheSame = x.hashCode();

        for (int i = 0; i < 30; i++) {
            assertThat(x.hashCode()).isEqualTo(alwaysTheSame);
        }
    }

    /**
     * If two objects are equal according to the equals(Object) method,
     * then calling the hashCode method on each of the two objects
     * must produce the same integer result.
     */
    @Property
    default void hashCodeIsConsistentWithEquals(@ForAll(EQUAL_DATAPOINTS) Tuple2<E, E> pair) {
        assertThat(pair.get1().hashCode()).isEqualTo(pair.get2().hashCode());
    }

    /**
     * toString of an object may be anything, but it may not throw exceptions.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Property
    default void toString(@ForAll(NONNULL_DATAPOINTS) E object) {
        assertThatNoException().isThrownBy(object::toString);
    }


    @Provide
    Arbitrary<? extends E> datapoints();

    @Provide
    default Arbitrary<? extends Tuple2<? extends E, ? extends E>> equalDatapoints() {
        return datapoints()
            .injectDuplicates(1)
            .tuple2()
            .filter(10_000, (t) -> t.get1().equals(t.get2()));
    }

    @Provide
    default Arbitrary<? extends  E> nonNullDatapoints() {
        return datapoints()
            .filter(Objects::nonNull);
    }

}
