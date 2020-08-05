package org.meeuw.math.abstractalgebra;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The algebraic structure that only defines multiplication. There might be no multiplicatie identity {@link MultiplicativeMonoid#one()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroup<E extends MultiplicativeSemiGroupElement<E>> extends AlgebraicStructure<E> {

    Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.MULTIPLICATION)));


    @Override
    default Set<Operator> supportedOperators() {
        return OPERATORS;
    }

}
