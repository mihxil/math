package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

import org.meeuw.math.numbers.Scalar;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class AbstractUncertainDouble<D extends UncertainDouble<D> & Scalar<D>>
    extends Number
    implements Comparable<D>, UncertainDouble<D>, Scalar<D> {

    @Override
    public long longValue() {
        return Math.round(doubleValue());
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public byte byteValue() {
        return (byte) longValue();
    }
    @Override
    public short shortValue() {
        return (short) longValue();
    }

    @Override
    public int compareTo(D o) {
        if (equals(o)) {
            return 0;
        }
        return Double.compare(doubleValue(), o.getValue());
    }

    @Override
    public double doubleValue() {
        return getValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(doubleValue());
    }

}
