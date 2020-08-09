package org.meeuw.math.uncertainnumbers;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class AbstractUncertainNumber<T extends AbstractUncertainNumber<T>> extends Number implements UncertainNumber<T>, AlgebraicElement<T> {

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return AlgebraicElementFormatProvider.getFormat(this, 4).format(this);
    }

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

    public int compareTo(T o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public T self() {
        return (T) this;
    }

}
