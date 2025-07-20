package org.meeuw.math.arithmetic.rpn;

import lombok.Getter;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

@Getter
public class Value<E extends AlgebraicElement<E>> implements StackElement<E> {

    private final E value;

    public Value(E value) {
        this.value = value;
    }
}
