package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<F extends FieldElement<F, A>, A extends Field<F, A>> extends MultiplicativeGroup<F, A>, AdditionGroup<F, A> {

    @Override
    default boolean supports(Operator operator) {
        return true;
    }

}
