package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * Like a {@link Ring} but without multiplicative identity.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<F extends RngElement<F>> extends AdditiveGroup<F>, MultiplicativeSemiGroup<F> {

    Set<Operator> operators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.ADDITION, Operator.SUBTRACTION, Operator.MULTIPLICATION)));

    @Override
    default Set<Operator> supportedOperators() {
        return operators;
    }


}
