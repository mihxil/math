package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroup<F extends AdditiveGroupElement<F, A>, A extends AdditiveGroup<F, A>> extends AlgebraicStructure<F, A> {

    Set<Operator> operators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.ADDITION, Operator.SUBTRACTION)));

    /**
     * The addition group by definition has an element that is 'zero'.
     */
    F zero();

    @Override
    default Set<Operator> supportedOperators() {
        return operators;
    }



}
