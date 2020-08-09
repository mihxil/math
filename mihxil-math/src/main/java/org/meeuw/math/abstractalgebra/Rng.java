package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * Like a {@link Ring} but without multiplicative identity.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<E extends RngElement<E>> extends AdditiveGroup<E>, MultiplicativeSemiGroup<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.ADDITION, Operator.SUBTRACTION, Operator.MULTIPLICATION)));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }


}
