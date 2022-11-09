package org.meeuw.math;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.*;

/**
 *
 * @since 0.9
 */
public final class BigDecimalUtils {

    private BigDecimalUtils() {}

    public static BigDecimal uncertaintyForBigDecimal(BigDecimal bigDecimal, MathContext context) {
        int scale = bigDecimal.scale();
        if (scale <= 0) { // whole number
            return BigDecimal.ZERO;
        }
        if (bigDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            // an integer too
            return BigDecimal.ZERO;
        }

        return BigDecimal.ONE.scaleByPowerOfTen(-1 * context.getPrecision()).stripTrailingZeros();
    }

    /**
     * An alternative implementation of {@link BigDecimalMath#pow(BigDecimal, BigDecimal, MathContext)}
     * I think this preserver the precision better.
     * <p>
     * It will correctly calculate things like {@code 200^-50} whereas {@link BigDecimalMath#pow} will make that {@code 0}
     * <pre>
     * x^b = 10 ^ b * log10(x) = 10 ^ y
     *
     * floor = floor(y)
     * rest = y - floor
     * </pre>
     *
     * Then {@code x^ b = 10 ^ rest}, with point shifted {@code floor} places to the right.
     * </p>
     *
     *
     */
    public static BigDecimal pow(BigDecimal value, BigDecimal exponent, MathContext context) {
        // Handle some special values.
        if (exponent.equals(BigDecimal.ZERO)) {
            return BigDecimal.ONE;
        }
        if (value.equals(BigDecimal.ZERO) && exponent.signum() == 1) {
            return BigDecimal.ZERO;
        }

        BigDecimal y = exponent.multiply(BigDecimalMath.log10(value, context));


        BigInteger floor = y.setScale(0, RoundingMode.FLOOR).unscaledValue();
        BigDecimal rest = y.subtract(new BigDecimal(floor));

        BigDecimal powOfRest = BigDecimalMath.pow(BigDecimal.TEN, rest, context);
        BigDecimal pow = powOfRest.movePointRight(floor.intValue());
        return pow;
    }
}
