package org.meeuw.math.exceptions;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

public class NotASubGroup extends MathException {
    public NotASubGroup(AlgebraicElement<?> e, Class<? extends AlgebraicElement<?>> clazz) {
        super("Cannot cast " + e.getClass() + " " + e + " to " + clazz);
    }
}
