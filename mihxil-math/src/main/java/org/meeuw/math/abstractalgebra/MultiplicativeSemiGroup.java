package org.meeuw.math.abstractalgebra;

import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableSet;
import static org.meeuw.math.abstractalgebra.Operator.MULTIPLICATION;

/**
 * The algebraic structure that only defines multiplication. There might be no multiplicatie identity {@link MultiplicativeMonoid#one()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroup<E extends MultiplicativeSemiGroupElement<E>>
    extends AlgebraicStructure<E> {

    Set<Operator> OPERATORS = unmodifiableSet(new HashSet<>(singletonList(MULTIPLICATION)));

    @Override
    default Set<Operator> getSupportedOperators() {
        return OPERATORS;
    }

}
