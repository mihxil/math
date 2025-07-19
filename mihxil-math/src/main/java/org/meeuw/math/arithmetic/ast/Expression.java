package org.meeuw.math.arithmetic.ast;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

public interface Expression<E extends AlgebraicElement<E>> {

    E eval();
}
