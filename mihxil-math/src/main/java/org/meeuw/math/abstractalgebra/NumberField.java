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

    @SuppressWarnings("unchecked")
    default E max(E e, E... es) {
        E result = e;
        for (E e2 : es) {
            if (e2.compareTo(result) > 0) {
                result = e2;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    default E min(E e, E... es) {
        E result = e;
        for (E e2 : es) {
            if (e2.compareTo(result) < 0) {
                result = e2;
            }
        }
        return result;
    }

}
