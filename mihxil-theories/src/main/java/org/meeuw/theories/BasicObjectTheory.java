package org.meeuw.theories;


import java.util.*;
import java.util.stream.Collectors;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.Tuple2;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


/**
 * Basic tests on {@link Object#equals(Object)}, {@link Object#hashCode()} and {@link Object#toString()}, which must probably be valid for <em>any</em> override of those.
 *
 * @author Michiel Meeuwissen
 * @since 0.10
 */
public interface BasicObjectTheory<E> {

    String DATAPOINTS = "datapoints";

    String DATAPOINTS_OR_NULL = "datapointsOrNull";

    String EQUAL_DATAPOINTS = "equalDatapoints";

    /**
     * For any non-null reference value x, x.equals(x) should return true
     */
    @SuppressWarnings("EqualsWithItself")
    @Property
    default void equalsIsReflexive(@ForAll(DATAPOINTS) Object x) {
        //System.out.println("reflexive " + x);
        assertThat(x.equals(x)).isTrue();
    }

    /**
     * For any non-null reference values x and y, x.equals(y)
     * should return true if and only if y.equals(x) returns true.
     */
    @Property
    default void equalsIsSymmetric(@ForAll(DATAPOINTS) Object x, @ForAll(DATAPOINTS) Object y) {
        //System.out.println("symetric = " + x + " " + y);
        assertThat(x.equals(y)).isEqualTo(y.equals(x));
    }

    /**
     * For any non-null reference values x, y, and z, if x.equals(y)
     * returns true and y.equals(z) returns true, then x.equals(z)
     * should return true.
     */
    @Property
    default  void equalsIsTransitive(
        @ForAll(EQUAL_DATAPOINTS) Tuple2<Object, Object> p1,
        @ForAll(EQUAL_DATAPOINTS) Tuple2<Object, Object> p2) {
        //System.out.println("transitive = " + p1 + " " + p2);
        assertThat(p1.get1().equals(p2.get2())).isEqualTo(p1.get2().equals(p2.get1()));
    }

    /**
     * For any non-null reference values x and y, multiple invocations
     * of x.equals(y) consistently return true  or consistently return
     * false, provided no information used in equals comparisons on
     * the objects is modified.
     */
    @Property
    default void equalsIsConsistent(@ForAll(DATAPOINTS) Object x, @ForAll(DATAPOINTS_OR_NULL) Object y) {
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
    default void equalsReturnFalseOnNull(@ForAll(DATAPOINTS) Object x) {
        assertThat(x.equals(null)).isFalse();
    }

    /**
     * For any non-null reference value x, x.equals(new Object); should
     * return false.
     */
    @SuppressWarnings("ConstantConditions")
    @Property
    default void equalsReturnFalseOnOtherObject(@ForAll(DATAPOINTS) Object x) {
        assertThat(x.equals(new Object())).isFalse();
    }

    /**
     * Whenever it is invoked on the same object more than once
     * the hashCode() method must consistently return the same
     * integer.
     */
    @Property
    default void hashCodeIsSelfConsistent(@ForAll(DATAPOINTS) Object x) {
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
    default void hashCodeIsConsistentWithEquals(@ForAll(EQUAL_DATAPOINTS) Tuple2<Object, Object> pair) {
        //System.out.println("hashCode consistent = " + pair + " " + pair.get1().hashCode());
        assertThat(pair.get1().hashCode()).isEqualTo(pair.get2().hashCode());
    }

    /**
     * toString of an object may be anything, but it may not throw exceptions.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Property
    default void toString(@ForAll(DATAPOINTS) Object object) {
        assertThatNoException().isThrownBy(object::toString);
    }


    /**
     * Provide non-{@code null} datapoints
     */
    @Provide
    Arbitrary<@NonNull E> datapoints();


    /**
     * The implementation for equals datapoints (see {@link #equalDatapoints()}.
     * Defaults to {@link Objects#equals(Object, Object)}.
     */
    default boolean equals(Object e1, Object e2) {
        return Objects.equals(e1, e2);
    }

    @Provide
    default Arbitrary<@NonNull? extends Tuple2<@NonNull Object, @NonNull Object>> equalDatapoints() {
        List<Object> samples = datapoints()
            .injectDuplicates(0.5)
            .sampleStream()
            .limit(1000)
            .collect(Collectors.toList());
        final java.util.Set<Tuple2<Object, Object>> setToReturn = new HashSet<>();
        final List<Object> check = new ArrayList<>();
        for (Object e : samples) {
            int i = -1;
            for (int j = 0; j < check.size(); j++) {
                if (equals(check.get(j), e)) {
                    i = j;
                    break;
                }
            }
            if (i == -1 ) {
                check.add(e);
            } else {
                setToReturn.add(Tuple.of(e, check.remove(i)));
            }
        }
        return Arbitraries.of(setToReturn);
    }

    @Provide
    default Arbitrary<@Nullable E> datapointsOrNull() {
        return datapoints()
            .injectNull(0.1);
    }

}
