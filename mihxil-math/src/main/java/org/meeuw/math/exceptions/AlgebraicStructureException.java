package org.meeuw.math.exceptions;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * Gets thrown if the argument of some operation is from the wrong algebraic structure.
 * Oftentimes - for singleton structures - this cannot happen, but some structures are parameterized, and different instances may
 * represent different, incompatible groups.
 * <p>
 * For some implementations, a similar issue may just result in a {@link ClassCastException}.
 *
 * @since 0.11
 */
public class AlgebraicStructureException extends MathException {

    public AlgebraicStructureException(AlgebraicElement<?> a, AlgebraicElement<?> b) {
        this(String.format("Structure %s of %s is not compatible with structure  %s of %s", a.getStructure(), a, b.getStructure(), b));
    }

    public AlgebraicStructureException(String s) {
        super(s);
    }
}
