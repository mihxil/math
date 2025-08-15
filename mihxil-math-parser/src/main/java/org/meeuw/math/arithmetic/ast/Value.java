package org.meeuw.math.arithmetic.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Value<E extends AlgebraicElement<E>> extends AbstractExpression<E> {

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
        return "(value:" + value.toString() + ")";
    }

    @Override
    public int compareTo(Expression<E> o) {
        if (o instanceof Value<?> otherValue) {
            return value.toString().compareTo(otherValue.value.toString());
        } else {
            return toString().compareTo(o.toString());
        }
    }
}
