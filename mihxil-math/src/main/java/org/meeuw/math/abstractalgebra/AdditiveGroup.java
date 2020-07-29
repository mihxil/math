package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 *  A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'addition'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroup<F extends AdditiveGroupElement<F>>   extends AlgebraicStructure<F>  {

    Set<Operator> operators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.ADDITION, Operator.SUBTRACTION)));

    /**
     * The additive group by definition has an element that is 'zero',  the additive identity element.
     */
    F zero();

    @Override
    default Set<Operator> supportedOperators() {
        return operators;
    }



}
