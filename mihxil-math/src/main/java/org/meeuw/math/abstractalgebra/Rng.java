package org.meeuw.math.abstractalgebra;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static org.meeuw.math.abstractalgebra.Operator.*;

/**
 * Like a {@link Ring} but without multiplicative identity.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<E extends RngElement<E>> extends AdditiveAbelianGroup<E>, MultiplicativeSemiGroup<E> {

    Set<Operator> OPERATORS = unmodifiableSet(new HashSet<>(asList(ADDITION, SUBTRACTION, MULTIPLICATION)));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
