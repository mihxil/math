package org.meeuw.theories;

import java.util.*;
import java.util.stream.Collectors;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple2;
import net.jqwik.api.Tuple.Tuple3;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.exceptions.NotComparableException;
import org.opentest4j.TestAbortedException;

import static java.lang.Integer.signum;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests basic properties for {@link Comparable} objects.
 * <ul>
 * <li>Normally, {@link #equalsConsistentWithComparable(Tuple2) equals must be consistent with comparable}</li>
 * <li>{@link #compareToNull(Comparable) comparing to null should raise NullPointerException}</li>
 * <li>{@link #compareToIsAntiCommutative(Comparable, Comparable) compare to is anti-commutative}</li>
 * <li>{@code compareTo} is also transitive ({@link #compareToIsTransitiveBigger}, {@link #compareToIsTransitiveSmaller}, {@link #compareToIsTransitiveEquals})</li>
 * @author Michiel Meeuwissen
 * @since 0.10
 */
public interface ComparableTheory<E extends Comparable<E>> extends BasicObjectTheory<E> {

    /**
     * This test whether compareTo {@link Comparable#compareTo(Object)} returns 0 iff if two object are equal.
     * TODO: This is not an absolute requirement, in some cases you may want to compareTo to zero even if two objects are not exactly equal.
     */
    @Property(maxDiscardRatio = 10000)
    default void equalsConsistentWithComparable(@ForAll(EQUAL_DATAPOINTS) Tuple2<? extends E, ? extends E> pair) {
        try {
            assertThat(pair.get1().compareTo(pair.get2())).isEqualTo(0);
        } catch (ClassCastException | NotComparableException exception) {
            throw new TestAbortedException();
        }
    }

    @SuppressWarnings({"ConstantConditions"})
    @Property
    default void compareToNull(@ForAll(DATAPOINTS) E x) {
        assertThatThrownBy(() -> x.compareTo(null)).isInstanceOf(NullPointerException.class);
    }

    /**
     * The implementor must ensure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y.
     */
    @Property
    default void compareToIsAntiCommutative(@ForAll(DATAPOINTS) E x, @ForAll(DATAPOINTS) E y) {
        try {
            assertThat(signum(x.compareTo(y))).isEqualTo(-1 * signum(y.compareTo(x)));
        } catch (ClassCastException | NotComparableException exception) {
            throw new TestAbortedException(exception.getClass().getName() + ":" + exception.getMessage() + " from " + x + "compareTo(" + y + "). This probably is acceptable");
        }
    }

    /**
     * The implementor must also ensure that the relation is transitive: (x.compareTo(y)>0 && y.compareTo(z)>0) implies x.compareTo(z)>0.
     */
    @Property(maxDiscardRatio = 1000)
    default void compareToIsTransitiveBigger(
        @ForAll(DATAPOINTS) E x,
        @ForAll(DATAPOINTS) E y,
        @ForAll(DATAPOINTS) E z) {
        try {

            Assume.that(x.compareTo(y) > 0);
            Assume.that(y.compareTo(z) > 0);

            assertThat(x.compareTo(z)).isGreaterThan(0);
        } catch (ClassCastException | NotComparableException exception) {
            throw new TestAbortedException(exception.getClass().getName() + ":" + exception.getMessage() + " from compareTo. This probably is acceptable. Relevant objects are " + x + ", " + y + " and " + z);

        }
    }

    /**
     * The implementor must also ensure that the relation is transitive: (x.compareTo(y)<0 && y.compareTo(z)<0) implies x.compareTo(z)<0.
     */
    @Property(maxDiscardRatio = 1000)
    default void compareToIsTransitiveSmaller(
        @ForAll(DATAPOINTS) E x,
        @ForAll(DATAPOINTS) E y,
        @ForAll(DATAPOINTS) E z) {
        try {
            Assume.that(x.compareTo(y) < 0);
            Assume.that(y.compareTo(z) < 0);

            assertThat(x.compareTo(z)).isLessThan(0);
        } catch (ClassCastException | NotComparableException exception) {
            throw new TestAbortedException(exception.getClass().getName() + ":" + exception.getMessage() + " from compareTo. This probably is acceptable. Relevant objects are " + x + ", " + y + " and " + z);
        }
    }

    /**
     * The implementor must also ensure that the relation is transitive: (x.compareTo(y)==0 && y.compareTo(z)==0) implies x.compareTo(z)==0.
     */
    @Property
    default void compareToIsTransitiveEquals(
        @ForAll("compareToEqualsDatapoints3") Tuple3<E, E, E> tuple) {
        assertThat(tuple.get1().compareTo(tuple.get2())).isEqualTo(0);
        assertThat(tuple.get2().compareTo(tuple.get3())).isEqualTo(0);
        assertThat(tuple.get1().compareTo(tuple.get3())).isEqualTo(0);
    }


    @Provide
    default Arbitrary<@NonNull Tuple3<@NonNull Comparator<?>, @NonNull Comparator<?>, @NonNull Comparator<?>>> compareToEqualsDatapoints3() {
        List<Object> samples = datapoints()
            .injectDuplicates(0.5)
            .sampleStream()
            .limit(5000)
            .collect(Collectors.toList());
        final List<Object> check = new ArrayList<>();
        final List<Tuple2<Comparable<?>, Comparable<?>>> set2ToReturn = new ArrayList<>();
        final List<Tuple3<Comparable<?>, Comparable<?>, Comparable<?>>> setToReturn = new ArrayList<>();
        SAMPLES:
        for (Object sampleObject : samples) {
            Comparable<?> sample = (Comparable<?>) sampleObject;
            Iterator<Object> i = check.iterator();
            while (i.hasNext()) {
                Comparable<?> toCheck = (Comparable<?>) i.next();
                try {

                    if (toCheck.compareTo(sample) == 0) {
                        set2ToReturn.add(Tuple.of(toCheck, sample));
                        i.remove();
                        continue SAMPLES;
                    }
                } catch (ClassCastException | NotComparableException exception) {
                    // ignore
                }
            }
            Iterator<Tuple2<Comparable<?>, Comparable<?>>> j = set2ToReturn.iterator();
            while (j.hasNext()) {
                Tuple2<Comparable<?>, Comparable<?>> toCheck = j.next();
                try {
                    if (toCheck.get2().compareTo(sample) == 0) {
                        setToReturn.add(Tuple.of(toCheck.get1(), toCheck.get2(), sample));
                        if (setToReturn.size() >= 10) {
                            break SAMPLES;
                        }
                        j.remove();
                        break;
                    }
                } catch (ClassCastException | NotComparableException exception) {
                    // ignore
                }
            }


            check.add(sample);
        }
        return Arbitraries.of(setToReturn);
    }


}

