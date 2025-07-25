package org.meeuw.math.arithmetic.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.meeuw.math.abstractalgebra.FieldElement;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Value<E extends FieldElement<E>> extends AbstractExpression<E> {

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

    @Override
    public int compareTo(Expression<E> o) {
        return value.toString().compareTo(o.toString());
    }
}
