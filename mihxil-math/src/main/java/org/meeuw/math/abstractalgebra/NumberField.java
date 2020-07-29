package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberField<F extends NumberFieldElement<F, A>, A extends NumberField<F, A>> extends
    Field<F, A> {

}
