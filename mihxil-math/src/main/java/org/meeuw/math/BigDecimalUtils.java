package org.meeuw.math;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.*;

/**
 *
 * @since 0.9
 */
public final class BigDecimalUtils {

    public static final BigDecimal ONE_HALF = new BigDecimal("0.5");

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

    public static BigDecimal pow10(int e) {
        BigDecimal result = ONE;
        while (e > 0) {
            result = result.multiply(TEN);
            e--;
        }
        while (e < 0) {
            result = result.divide(TEN, MathContext.UNLIMITED);
            e++;
        }
        return result;
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

        return BigDecimalMath.sqrt(value, context);
        ////Rounds small values to 0!
        //return pow(value, ONE_HALF, context);
    }
}
