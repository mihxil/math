package org.meeuw.math.abstractalgebra;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldElement<F extends FieldElement<F, A>, A extends Field<F, A>> extends
    MultiplicativeGroupElement<F, A>,
    AdditiveGroupElement<F, A> {


}
