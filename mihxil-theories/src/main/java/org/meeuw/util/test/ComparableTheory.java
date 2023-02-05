package org.meeuw.util.test;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple2;

import static java.lang.Integer.signum;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.10
 */
public interface ComparableTheory<E extends Comparable<E>> extends BasicObjectTheory<E> {

    /**
     * This test whether compareTo {@link Comparable#compareTo(Object)} returns 0 iff if two object are equal.
     * TODO: This is not an absolute requirement, in some cases you may want to compareTo to zero even if two objects are not exactly equal.
     *
     */
    @Property(maxDiscardRatio = 10000)
    default void  equalsConsistentWithComparable(@ForAll(EQUAL_DATAPOINTS) Tuple2<E, E> pair) {
        assertThat(pair.get1().compareTo(pair.get2())).isEqualTo(0);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    @Property
    default void compareToNull(@ForAll(NONNULL_DATAPOINTS) E x) {
        assertThatThrownBy(() -> x.compareTo(null)).isInstanceOf(NullPointerException.class);
    }

    /**
     * The implementor must ensure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y.
     */
    @Property
    default void compareToIsAntiCommutative(@ForAll(NONNULL_DATAPOINTS) E x, @ForAll(NONNULL_DATAPOINTS) E y) {
        assertThat(signum(x.compareTo(y))).isEqualTo(-1 * signum(y.compareTo(x)));

    }
    /**
     * The implementor must also ensure that the relation is transitive: (x.compareTo(y)>0 && y.compareTo(z)>0) implies x.compareTo(z)>0.
     */
    @Property(maxDiscardRatio = 1000)
    default void compareToIsTransitiveBigger(
        @ForAll(NONNULL_DATAPOINTS) E x,
        @ForAll(NONNULL_DATAPOINTS) E y,
        @ForAll(NONNULL_DATAPOINTS) E z) {

        Assume.that(x.compareTo(y) > 0);
        Assume.that(y.compareTo(z) > 0);

        assertThat(x.compareTo(z)).isGreaterThan(0);
    }
    @Property(maxDiscardRatio = 1000)
    default void compareToIsTransitiveSmaller(
        @ForAll(NONNULL_DATAPOINTS) E x,
        @ForAll(NONNULL_DATAPOINTS) E y,
        @ForAll(NONNULL_DATAPOINTS) E z) {

          Assume.that(x.compareTo(y) < 0);
          Assume.that(y.compareTo(z) < 0);

          assertThat(x.compareTo(z)).isLessThan(0);
    }

    @Property(maxDiscardRatio = 1000)
    default void compareToIsTransitiveEquals(
        @ForAll(NONNULL_DATAPOINTS) E x,
        @ForAll(NONNULL_DATAPOINTS) E y,
        @ForAll(NONNULL_DATAPOINTS) E z) {

        Assume.that(x.compareTo(y) == 0);
        Assume.that(y.compareTo(z) == 0);

        assertThat(x.compareTo(z)).isEqualTo(0);
    }
}