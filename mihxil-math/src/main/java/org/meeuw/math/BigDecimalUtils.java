package org.meeuw.math;

import java.math.BigDecimal;
import java.math.MathContext;

import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.text.TextUtils;

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

    /**
     * @return A {@link BigDecimal} representing 10<sup>e</sup>
     * @param e The (integer) exponent
     */
    public static BigDecimal pow10(int e) {
        return pow(TEN, e, MathContext.UNLIMITED);
    }

    /**
     * @return A {@link BigDecimal} representing base<sup>e</sup>
     * @param base The base of the power to take
     * @param e The (integer) exponent
     */
    public static BigDecimal pow(BigDecimal base, long e, MathContext context) {
        BigDecimal result = ONE;
        if (base.equals(ZERO)) {
            if (e <= 0) {
                throw new IllegalPowerException("Cannot raise to negative power", base + TextUtils.superscript(e));
            }
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

}
