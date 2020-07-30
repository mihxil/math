package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<F extends FieldElement<F>> extends
    MultiplicativeGroup<F>,
    Ring<F> {

    Set<Operator> operators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.values())));

    @Override
    default boolean supports(Operator operator) {
        return true;
    }

    @Override
    default Set<Operator> supportedOperators() {
        return operators;

    }
}
