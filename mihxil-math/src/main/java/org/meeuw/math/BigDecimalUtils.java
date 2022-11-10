package org.meeuw.math;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.*;

import static ch.obermuhlner.math.big.BigDecimalMath.exp;
import static ch.obermuhlner.math.big.BigDecimalMath.log10;
import static java.math.BigDecimal.*;
import static java.math.RoundingMode.FLOOR;

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
            return ZERO;
        }
        if (bigDecimal.remainder(ONE).compareTo(ZERO) == 0) {
            // an integer too
            return ZERO;
        }

        return ONE.scaleByPowerOfTen(-1 * context.getPrecision()).stripTrailingZeros();
    }

    /**
     * An alternative implementation of {@link BigDecimalMath#pow(BigDecimal, BigDecimal, MathContext)}
     * I think this preserves the precision better, and will also work for large negative powers.
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
    public static final BigDecimal LOG_OF_10 = BigDecimalMath.log(TEN, MathContext.DECIMAL128);
    public static BigDecimal pow(BigDecimal value, BigDecimal exponent, MathContext context) {
        // Handle some special values.
        if (exponent.equals(ZERO)) {
            return ONE;
        }
        if (value.equals(ZERO) && exponent.signum() == 1) {
            return ZERO;
        }
        MathContext mathContext = new MathContext(context.getPrecision() + 6);
        try {
			long longValue = exponent.longValueExact();

            BigDecimal pow =  pow(value, Math.abs(longValue), mathContext);
            if (longValue < 0) {
                return ONE.divide(pow, mathContext).round(context);
            } else {
                return pow.round(context);
            }

		} catch (ArithmeticException ex) {
			// ignored
		}
        BigDecimal y = exponent.multiply(log10(value, mathContext));


        BigInteger floor = y.setScale(0, FLOOR).unscaledValue();
        BigDecimal rest = y.subtract(new BigDecimal(floor));

        BigDecimal powOfRest = pow10(rest, mathContext);
        BigDecimal pow = powOfRest.scaleByPowerOfTen(floor.intValue());
        return pow.round(context);
    }

    public static BigDecimal pow10(BigDecimal exponent, MathContext mathContext) {
        return exp(exponent.multiply(LOG_OF_10, mathContext), mathContext);
    }
    public static BigDecimal pow(BigDecimal base, long e, MathContext context) {
        BigDecimal result = ONE;
        if (base.equals(ZERO)) {
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

    /**
     * Similarly {@link BigDecimalMath#sqrt(BigDecimal, MathContext)} has a problem with the square root of very small numbers. They will result zero.
     * <p>
     * This doesn't.
     */
    public static BigDecimal sqrt(BigDecimal value, MathContext context) {

        //return BigDecimalMath.sqrt(value, context); //Rounds small values to 0!
        return pow(value, HALF, context);
    }
}
