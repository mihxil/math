package org.meeuw.math;
/**
 * @since 0.9
 */
public final class DoubleUtils {

    private DoubleUtils() {}

    /**
     * Returns 10 to the power of some integer {@code e}, a utility in java.lang.Math for that lacks.
     * @param e the exponent
     * @return 10<sup>e</sup>
     */
    public static double pow10(int e) {
        return pow(10, e);
    }

    /**
     * Returns 2 to the power of some integer {@code e}, a utility in java.lang.Math for that lacks.
     * @param e the exponent
     * @return 2<sup>e</sup>
     */
    public static double pow2(int e) {
        return pow(2, e);
    }

    /**
     * Returns {@code base} to the power {@code e}, using integer operations only.
     *
     * @param base the base
     * @param e the exponent
     * @return base<sup>e</sup>
     */
    public static double pow(int base, int e) {
        double result = 1;
        while (e > 0) {
            result *= base;
            e--;
        }
        while (e < 0) {
            result /= base;
            e++;
        }
        assert e == 0;
        return result;
    }

    /**
     * Returns base to the power of an integer, a utility in java.lang.Math for that lacks.
     * @param base the base
     * @param e the exponent
     * @return base<sup>e</sup>
     */
    public static double pow(double base, int e) {
        double result = 1;
        if (base == 0) {
            return result;
        }
        // branching will make this slow

        // Math.pow(base, i); will probably perform better?
        while (e > 0) {
            result *= base;
            e--;
        }
        while (e < 0) {
            result /= base;
            e++;
        }
        assert e == 0;
        return result;
    }

    /**
     * A crude order of magnitude implemention
     * <p>
     * This is like {@code Math.log10(Mat.abs(d))}
     * @param d a double
     * @return <sup>10</sup>log(d)
     */
    public static int log10(double d) {
        return (int) Math.floor(Math.log10(Math.abs(d)));
    }

    /**
     * pow(2, &lt;this value&gt;) is an estimate of the 'uncertainty' in this double. More precision it simply cannot represent.
     * @param doubleValue a double value
     * @return the bit the power of 2 exponent that the least significant bit in the IEEE 754 representation of the double represents.
     */
    public static int leastSignificantBit(double doubleValue) {
        long exponent = (int) ((((Double.doubleToLongBits(doubleValue) & 0x7ff0000000000000L) >> 52) - 1023) - 51);
        return (int) exponent;
    }

    public static double uncertaintyForDouble(double doubleValue) {
        return pow2(leastSignificantBit(doubleValue));
    }

    public static double max(double... values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double tmp : values) {
            max = Math.max(max, tmp);
        }
        return max;
    }

    /**
     * @see Math#round(double)
     * @throws IllegalArgumentException if {@code value} is {@code NaN}
     */
    public static long round(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("Cannot round " + value + " to a long");
        }
        return Math.round(value);
    }
}
