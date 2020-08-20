package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.VectorInterface;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorSpaceTheory<E extends FieldElement<E>, V extends VectorInterface<E, V>> extends ElementTheory<E> {

    String VECTORS = "vectors";


    @Property
    default void dimension(@ForAll(VECTORS) V v1) {
        int dim = v1.getSpace().getDimension();
        assertThat(dim).isGreaterThan(0);

        for (int i = 0 ; i < dim; i++) {
            v1.get(i);
        }
        AtomicInteger count = new AtomicInteger(0);
        v1.forEach(e -> {
            count.incrementAndGet();
        });
        assertThat(count.intValue()).isEqualTo(dim);

        count.set(0);
        v1.spliterator().forEachRemaining(
            e -> {
            count.incrementAndGet();
        }
        );
        assertThat(count.intValue()).isEqualTo(dim);

        assertThatThrownBy(() -> {
            v1.get(dim);
        }).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Property
    default void toString(@ForAll(VECTORS) V v1) {
        String toString = v1.toString();
        v1.forEach(e -> {
            assertThat(toString).contains(e.toString());
        });
    }


    @Property
    default void space(@ForAll(VECTORS) V v1, @ForAll(VECTORS) V v2) {
        assertThat(v1.getSpace().getDimension()).isEqualTo(v2.getSpace().getDimension());
        assertThat(v1.getSpace().getField()).isEqualTo(v2.getSpace().getField());
        assertThat(v1.getSpace().zero()).isEqualTo(v2.getSpace().zero());
        assertThat(v1.getSpace()).isEqualTo(v2.getSpace());
        assertThat(v1.getSpace()).isSameAs(v2.getSpace());
        assertThat(v1.getSpace().toString()).contains(v1.getSpace().getField().toString());
    }

    @Property
    default void associativity(@ForAll(VECTORS) V v1, @ForAll(VECTORS) V v2, @ForAll(VECTORS) V v3) {
        assertThat(v1.plus(v2.plus(v3))).isEqualTo((v1.plus(v2)).plus(v3));
    }

    @Property
    default void commutativity(@ForAll(VECTORS) V v1, @ForAll(VECTORS) V v2) {
        assertThat(v1.plus(v2)).isEqualTo(v2.plus(v1));
    }

    @Property
    default void inverse(@ForAll(VECTORS) V v1) {
        assertThat(v1.plus(v1.inverse())).isEqualTo(v1.getSpace().zero());
    }

    @Property
    default void compatibility(@ForAll(VECTORS) V v, @ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat((v.times(a)).times(b)).isEqualTo(v.times(a.times(b)));
    }


    @Property
    default void scalarIdentity(@ForAll(VECTORS) V v) {
        assertThat(v.times(v.getSpace().getField().one())).isEqualTo(v);
    }


    @Property
    default void vectorDistributivity(@ForAll(VECTORS) V v1, @ForAll(VECTORS) V v2, @ForAll(ELEMENTS) E e) {
        assertThat((v1.plus(v2)).times(e)).isEqualTo((v1.times(e)).plus(v2.times(e)));
    }


    @Property
    default void scalarDistributivity(@ForAll(VECTORS) V v, @ForAll(ELEMENTS) E e1, @ForAll(ELEMENTS) E e2) {
        assertThat((v.times(e1.plus(e2)))).isEqualTo((v.times(e1)).plus(v.times(e2)));
    }


    @Provide
    Arbitrary<? extends V> vectors();

}
