package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup;
import org.meeuw.math.abstractalgebra.AdditiveSemiGroupElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveAbelianSemiGroupTheory<E extends AdditiveSemiGroupElement<E>>
    extends AdditiveSemiGroupTheory<E> {

    @Property
    default void additiveCommutativity (
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {
        assertThat(v1.plus(v2)).isEqualTo(v2.plus(v1));
    }

    @Property
    default void additiveCommutativityProperty(
        @ForAll(STRUCTURE) AdditiveAbelianSemiGroup<E> group) {
        assertThat(group.additionIsCommutative()).isTrue();
    }

}
