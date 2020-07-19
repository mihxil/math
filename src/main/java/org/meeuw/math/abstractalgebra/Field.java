package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<F extends FieldElement<F, A>, A extends Field<F, A>> extends Group<F, A> {

    F zero();
}
