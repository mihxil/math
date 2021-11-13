package org.meeuw.math.abstractalgebra;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableNavigableSet;
import static java.util.Collections.unmodifiableSortedSet;

import org.meeuw.math.Utils;

import static org.meeuw.math.abstractalgebra.Operator.*;

/**
 * Like a {@link Ring} but without multiplicative identity.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<E extends RngElement<E>> extends AdditiveAbelianGroup<E>, MultiplicativeSemiGroup<E> {

    NavigableSet<Operator> OPERATORS = Utils.navigableSet(ADDITION, SUBTRACTION, MULTIPLICATION);

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
