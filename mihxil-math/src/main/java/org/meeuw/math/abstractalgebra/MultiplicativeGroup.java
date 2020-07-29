package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'multiplication'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroup<F extends MultiplicativeGroupElement<F>> extends AlgebraicStructure<F> {

    Set<Operator> operators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.MULTIPLICATION, Operator.DIVISION)));

    /**
     * The multiplicative group by definition has an element that is 'one', the multiplicative identity element.
     */
    F one();

    @Override
    default Set<Operator> supportedOperators() {
        return operators;
    }

}
