package org.meeuw.math.uncertainnumbers.field;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

/**
 * An element of the {@link UncertainRealField}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainReal
    extends
    UncertainDouble<UncertainReal>,
    CompleteScalarFieldElement<UncertainReal> {

    @Override
    UncertainReal negation();

    @Override
    default UncertainReal times(UncertainReal multiplier) {
        return UncertainDouble.super.times(multiplier);
    }

    @Override
    UncertainReal pow(int n);

    @Override
    default UncertainReal plus(UncertainReal summand) {
        return UncertainDouble.super.plus(summand);
    }

    @Override
    default boolean isZero() {
        return isExact() && CompleteScalarFieldElement.super.isZero();
    }

    @Override
    default boolean isOne() {
        return isExact() && CompleteScalarFieldElement.super.isOne();
    }

}
