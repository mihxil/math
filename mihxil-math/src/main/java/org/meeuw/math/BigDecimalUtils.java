package org.meeuw.math;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.*;

import static ch.obermuhlner.math.big.BigDecimalMath.exp;

/**
 *
 * @since 0.9
 */
public final class BigDecimalUtils {

    public static final BigDecimal HALF = new BigDecimal("0.5");

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
    public static final BigDecimal LOG_OF_10 = BigDecimalMath.log(BigDecimal.TEN, MathContext.DECIMAL128);
    public static BigDecimal pow(BigDecimal value, BigDecimal exponent, MathContext context) {
        // Handle some special values.
        if (exponent.equals(BigDecimal.ZERO)) {
            return BigDecimal.ONE;
        }
        if (value.equals(BigDecimal.ZERO) && exponent.signum() == 1) {
            return BigDecimal.ZERO;
        }
        MathContext mathContext = new MathContext(context.getPrecision() + 6);
        try {
			long longValue = exponent.longValueExact();

            BigDecimal pow =  pow(value, Math.abs(longValue), mathContext);
            if (longValue < 0) {
                return BigDecimal.ONE.divide(pow, mathContext).round(context);
            } else {
                return pow.round(context);
            }

		} catch (ArithmeticException ex) {
			// ignored
		}
        BigDecimal y = exponent.multiply(BigDecimalMath.log10(value, mathContext));


        BigInteger floor = y.setScale(0, RoundingMode.FLOOR).unscaledValue();
        BigDecimal rest = y.subtract(new BigDecimal(floor));

        BigDecimal powOfRest = pow10(rest, mathContext);
        BigDecimal pow = powOfRest.scaleByPowerOfTen(floor.intValue());
        return pow.round(context);
    }

    public static BigDecimal pow10(BigDecimal exponent, MathContext mathContext) {
        return exp(exponent.multiply(LOG_OF_10, mathContext), mathContext);
    }
    public static BigDecimal pow(BigDecimal base, long e, MathContext context) {
        BigDecimal result = BigDecimal.ONE;
        if (base.equals(BigDecimal.ZERO)) {
            return result;
        }
        // branching will make this slow
        while (e > 0) {
            result = result.multiply(base, context);
            e--;
        }
        while (e < 0) {
            result = result.divide(base, context);
            e++;
        }
        assert e == 0;
        return result;
    }

    public static BigDecimal sqrt(BigDecimal value, MathContext context) {

        ///return BigDecimalMath.sqrt(value, context); Rounds small values to 0!
        return pow(value, HALF, context);
    }
}
