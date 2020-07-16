package org.meeuw.math.statistics;

import lombok.Getter;

import org.meeuw.math.Utils;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class AbstractUncertainNumber<T extends AbstractUncertainNumber<T>> extends Number implements UncertainNumber<T> {

    /**
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     *
     * This is used in {@link #toString()}
     */
    @Getter
    protected int minimumExponent = 4;


    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        if (! isExact()) {
            return Utils.scientificNotationWithUncertaintity(doubleValue(), getUncertainty(), minimumExponent);
        } else {
            return Utils.scientificNotation(doubleValue(), minimumExponent);
        }
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
    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }


}
