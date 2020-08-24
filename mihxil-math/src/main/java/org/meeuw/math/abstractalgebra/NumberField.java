package org.meeuw.math.abstractalgebra;

import org.meeuw.math.Equivalence;

/**
 * A field of {@link NumberFieldElement}'s. That is, its elements are also {@link NumberFieldElement}s.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberField<E extends NumberFieldElement<E>> extends Field<E>  {

    @Override
    default Equivalence<E> getEquivalence() {
        return NumberFieldElement::equalsWithEpsilon;
    }

    default E max(E e1, E e2) {
        return e1.compareTo(e2 ) > 0 ? e1 : e2;
    }

}
