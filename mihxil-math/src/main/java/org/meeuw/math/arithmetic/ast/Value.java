package org.meeuw.math.arithmetic.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

@Getter
@EqualsAndHashCode
public class Value<E extends AlgebraicElement<E>> implements Expression<E> {

    private final E value;

    public Value(E value) {
        this.value = value;
    }

    @Override
    public E eval() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
