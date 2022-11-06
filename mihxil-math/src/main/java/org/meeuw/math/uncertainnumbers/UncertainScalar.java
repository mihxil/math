package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

import org.meeuw.math.numbers.Scalar;

/**
 * A {@link UncertainNumber} that is a {@link Scalar} too.
 * @since 0.9
 */
public interface UncertainScalar<N extends Number, SELF extends UncertainScalar<N, SELF>>
    extends  UncertainNumber<N>, Scalar<SELF> {

    @Override
    default int signum() {
        return (int) Math.signum(doubleValue());
    }

    @Override
    default BigDecimal bigDecimalValue() {
        return UncertainNumber.super.bigDecimalValue();
    }
}
