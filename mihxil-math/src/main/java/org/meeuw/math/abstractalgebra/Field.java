package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<E extends FieldElement<E>> extends
    MultiplicativeAbelianGroup<E>,
    Ring<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.values())));


    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;

    }
}
