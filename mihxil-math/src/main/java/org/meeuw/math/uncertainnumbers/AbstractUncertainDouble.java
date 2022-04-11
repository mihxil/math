package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class AbstractUncertainDouble<D extends UncertainDouble<D>>
    extends Number
    implements Comparable<D>, UncertainDouble<D> {



    @Override
    public int compareTo(D o) {
        if (equals(o)) {
            return 0;
        }
        return Double.compare(doubleValue(), o.getValue());
    }


    @Override
    public int intValue() {
        return UncertainDouble.super.intValue();
    }

    @Override
    public float floatValue() {
        return UncertainDouble.super.floatValue();
    }

    @Override
    public double doubleValue() {
        return getValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(doubleValue());
    }

    @Override
    public D plus(D summand) {
        return UncertainDouble.super.plus(summand);
    }


}
