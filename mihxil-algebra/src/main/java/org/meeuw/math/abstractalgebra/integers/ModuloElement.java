package org.meeuw.math.abstractalgebra.integers;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.RingElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public abstract class ModuloElement<E extends ModuloElement<E, S>, S extends ModuloStructure<E, S>>
    implements RingElement<E>, Serializable {

    //@Serial
    private static final long serialVersionUID = 0L;

    @Getter
    final int value;
    final S structure;

    ModuloElement(int value, S structure) {
        this.value = value % structure.divisor;
        this.structure = structure;
    }

    @Override
    public S getStructure() {
        return structure;
    }

    @Override
    public E times(E multiplier) {
        return structure.element(value * multiplier.value);
    }

    @Override
    public E plus(E summand) {
        return structure.element(value + summand.value);
    }

    @Override
    public E negation() {
        return structure.element(-1 * value );
    }

    @Override
    public String toString() {
        return String.valueOf(value); /* + "%" + structure.divisor;*/
    }

}
