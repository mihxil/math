package org.meeuw.math.abstractalgebra;

import java.util.*;

/**
 * The algebraic structure that only defines multiplication. There might be no multiplicatie identity {@link MultiplicativeMonoid#one()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroup<E extends AdditiveSemiGroupElement<E>> extends AlgebraicStructure<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(Operator.ADDITION)));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
