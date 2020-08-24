package org.meeuw.math.abstractalgebra;

import org.meeuw.math.Equivalence;
import org.meeuw.math.numbers.NumberElement;

/**
 * A field of {@link NumberFieldElement}'s. That is, its elements are also {@link NumberElement}s.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberField<E extends NumberFieldElement<E>> extends Field<E>  {

    @Override
    default Equivalence<E> getEquivalence() {
        return NumberFieldElement::equalsWithEpsilon;
    }

}
