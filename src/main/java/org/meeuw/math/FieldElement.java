package org.meeuw.math;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldElement<F extends FieldElement<F>> extends GroupElement<F> {

    F plus(F summand);

    default F minus(F subtrahend) {
        return plus(subtrahend.negate());
    }
    F negate();

}
