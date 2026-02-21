package org.meeuw.math;

import java.math.BigInteger;

/**
 * @since 0.9
 */
public final class DoubleUtils {

    private DoubleUtils() {
        // no instances
    }

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
     * A crude order of magnitude implementation
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

    /**
     * If uncertain double is not presented with an error, you may guess that is in the order of the number of shown digits.
     */
    public static double implicitUncertaintyForDouble(double doubleValue, String string) {
        if (doubleValue < 1) {
            while (string.startsWith("0")) {
                string = string.substring(1);
            }
        }
        string = string.replace(".", "");

        int order = log10(doubleValue);
        return 5 * Math.pow(10, order  - string.length());
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

    /**
     * Fast bit-level check whether the mathematical product of two (finite) doubles is exactly representable as a double.
     * This works by decomposing each double into an integer significand and a binary exponent (so the value = significand * 2^exponent),
     * computing the exact integer product of the significands and combined exponent, and comparing that exact value with the
     * actual double product's decomposition. Uses purely integer operations (BigInteger) and avoids BigDecimal.
     *
     * Note: returns false for NaN or infinite products; returns true for exact zero products.
     */
    public static boolean isExactProduct(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) return false;
        if (Double.isInfinite(a) || Double.isInfinite(b)) return false;
        // zero product is exactly representable
        if (a == 0.0 || b == 0.0) return true;

        double prod = a * b;
        if (Double.isInfinite(prod) || Double.isNaN(prod)) return false;

        // sign check
        boolean signAB = ((Double.doubleToLongBits(a) ^ Double.doubleToLongBits(b)) & 0x8000000000000000L) != 0;
        boolean signProd = (Double.doubleToLongBits(prod) & 0x8000000000000000L) != 0;
        if (signAB != signProd) {
            return false;
        }

        // decompose a
        long bitsA = Double.doubleToLongBits(a);
        int expA = (int) ((bitsA & 0x7ff0000000000000L) >> 52);
        long manA = bitsA & 0x000fffffffffffffL;
        BigInteger mA;
        int eA;
        if (expA == 0) {
            // subnormal
            mA = BigInteger.valueOf(manA);
            eA = 1 - 1023 - 52; // -1074
        } else {
            mA = BigInteger.valueOf((1L << 52) | manA);
            eA = expA - 1023 - 52;
        }

        // decompose b
        long bitsB = Double.doubleToLongBits(b);
        int expB = (int) ((bitsB & 0x7ff0000000000000L) >> 52);
        long manB = bitsB & 0x000fffffffffffffL;
        BigInteger mB;
        int eB;
        if (expB == 0) {
            mB = BigInteger.valueOf(manB);
            eB = 1 - 1023 - 52;
        } else {
            mB = BigInteger.valueOf((1L << 52) | manB);
            eB = expB - 1023 - 52;
        }

        BigInteger mP = mA.multiply(mB);
        int eP = eA + eB;

        // decompose actual product
        long bitsP = Double.doubleToLongBits(prod);
        int expP = (int) ((bitsP & 0x7ff0000000000000L) >> 52);
        long manP = bitsP & 0x000fffffffffffffL;
        BigInteger mProd;
        int eProd;
        if (expP == 0) {
            mProd = BigInteger.valueOf(manP);
            eProd = 1 - 1023 - 52;
        } else {
            mProd = BigInteger.valueOf((1L << 52) | manP);
            eProd = expP - 1023 - 52;
        }

        int delta = eP - eProd;
        if (delta >= 0) {
            return mP.shiftLeft(delta).equals(mProd);
        } else {
            return mP.equals(mProd.shiftLeft(-delta));
        }
    }

    /**
     * Variant to check whether a double times a (possibly large) integer {@link BigInteger} is exactly representable as a double.
     */
    public static boolean isExactProduct(double a, BigInteger multiplier) {
        if (Double.isNaN(a) || multiplier == null) return false;
        if (Double.isInfinite(a)) return false;
        if (a == 0.0 || multiplier.signum() == 0) return true;

        double dMultiplier = multiplier.doubleValue();
        double prod = a * dMultiplier;
        if (Double.isInfinite(prod) || Double.isNaN(prod)) return false;

        // sign check
        boolean signAB = ((Double.doubleToLongBits(a) & 0x8000000000000000L) != 0) ^ (multiplier.signum() < 0);
        boolean signProd = (Double.doubleToLongBits(prod) & 0x8000000000000000L) != 0;
        if (signAB != signProd) {
            return false;
        }

        // decompose a
        long bitsA = Double.doubleToLongBits(a);
        int expA = (int) ((bitsA & 0x7ff0000000000000L) >> 52);
        long manA = bitsA & 0x000fffffffffffffL;
        BigInteger mA;
        int eA;
        if (expA == 0) {
            mA = BigInteger.valueOf(manA);
            eA = 1 - 1023 - 52; // -1074
        } else {
            mA = BigInteger.valueOf((1L << 52) | manA);
            eA = expA - 1023 - 52;
        }

        BigInteger mP = mA.multiply(multiplier.abs());
        int eP = eA;

        // decompose actual product
        long bitsP = Double.doubleToLongBits(prod);
        int expP = (int) ((bitsP & 0x7ff0000000000000L) >> 52);
        long manP = bitsP & 0x000fffffffffffffL;
        BigInteger mProd;
        int eProd;
        if (expP == 0) {
            mProd = BigInteger.valueOf(manP);
            eProd = 1 - 1023 - 52;
        } else {
            mProd = BigInteger.valueOf((1L << 52) | manP);
            eProd = expP - 1023 - 52;
        }

        int delta = eP - eProd;
        if (delta >= 0) {
            return mP.shiftLeft(delta).equals(mProd);
        } else {
            return mP.equals(mProd.shiftLeft(-delta));
        }
    }
}
