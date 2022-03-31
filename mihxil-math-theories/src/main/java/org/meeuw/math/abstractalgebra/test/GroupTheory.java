package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InverseException;
import org.meeuw.math.exceptions.NotASubGroup;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface GroupTheory<E extends GroupElement<E>>
    extends MagmaTheory<E> {

    @Property
    default void groupOperators(@ForAll(STRUCTURE) Group<E> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.OPERATION);
    }

    @Property
    default void groupUnitaryOperators(@ForAll(STRUCTURE) Group<E> s) {
        assertThat(s.getSupportedUnaryOperators()).contains(BasicAlgebraicUnaryOperator.INVERSION);
    }

    @Property
    default void operateAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.operate(v2)).operate(v3)).isEqualTo(v1.operate((v2.operate(v3))));
    }

    @Property
    default void unity(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.operate(v.getStructure().unity()).equals(v)).isTrue();
    }


    @Property
    default void inverse(
        @ForAll(ELEMENTS) E v) {
        try {
            assertThat(v.inverse().operate(v).eq(v.getStructure().unity())).withFailMessage(() -> "inverse " + v.inverse() + " * " + v + " != " + v.getStructure().unity()).isTrue();
        } catch (InverseException ie) {
            getLogger().info(ie.getMessage());
        }
    }

    class UnknownGroupElement implements GroupElement<UnknownGroupElement> {

        @Override
        public UnknownGroup getStructure() {
            return null;
        }

        @Override
        public UnknownGroupElement operate(UnknownGroupElement operand) {
            return null;
        }

        @Override
        public UnknownGroupElement inverse() {
            return null;
        }
    }
    class UnknownGroup implements Group<UnknownGroupElement> {

        @Override
        public Cardinality getCardinality() {
            return null;
        }

        @Override
        public Class<UnknownGroupElement> getElementClass() {
            return UnknownGroupElement.class;
        }

        @Override
        public UnknownGroupElement unity() {
            return null;
        }
    }

    @Property
    default void castingError(@ForAll(ELEMENTS) E v) {
        assertThatThrownBy(() -> {
            v.cast(UnknownGroupElement.class);
        }).isInstanceOf(NotASubGroup.class);

    }


}
