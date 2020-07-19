package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface AlgebraicStructure<F> {

    boolean supports(Operator operator);
}
