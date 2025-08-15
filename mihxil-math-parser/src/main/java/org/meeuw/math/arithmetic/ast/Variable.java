package org.meeuw.math.arithmetic.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Supplier;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Variable<E extends AlgebraicElement<E>> extends AbstractExpression<E> {

    private final Supplier<E> value;
    private final String name;

    public Variable(String name, Supplier<E> value) {
        this.value = value;
        this.name = name;
    }
    @Override
    public E eval() {
        return value.get();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Expression<E> o) {
        return name.compareTo(o.toString());
    }
}
