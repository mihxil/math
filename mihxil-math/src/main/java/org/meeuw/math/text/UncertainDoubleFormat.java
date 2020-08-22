package org.meeuw.math.text;

import lombok.*;

import java.text.*;
import java.util.Locale;

import org.meeuw.math.Utils;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormat extends Format {

    static final int VALUE_FIELD = 0;
    static final int UNCERTAINTITY_FIELD = 1;

    public static final String TIMES = "\u00B7";  /* "·10' */
    public static final String TIMES_10 = TIMES + "10";  /* "·10' */


    /**
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     *
     * This is used in {@link #toString()}
     */
    @Getter
    @Setter
    private int minimumExponent = 4;

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        if (number instanceof UncertainDouble) {
            UncertainDouble uncertainNumber = (UncertainDouble) number;
            if (uncertainNumber.isExact()) {
                toAppendTo.append(scientificNotation(uncertainNumber.doubleValue(), minimumExponent));
            } else {
                toAppendTo.append(scientificNotationWithUncertainty(uncertainNumber.doubleValue(), uncertainNumber.getUncertainty(), minimumExponent));
            }
            return toAppendTo;
        } else {
            throw new IllegalArgumentException("Cannot format given Object as a Number");
        }
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }


    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
     public static String scientificNotationWithUncertainty(double meanDouble, double stdDouble, int minimumExponent) {
        SplitNumber std  = SplitNumber.split(stdDouble);
        SplitNumber mean = SplitNumber.split(meanDouble);
        boolean largeError = Math.abs(stdDouble) > Math.abs(meanDouble);

        // use difference of order of magnitude of std to determin how mean digits of the mean are
        // relevant
        int magnitudeDifference = mean.exponent - std.exponent;
        //System.out.println("Md: " + mean + " " + std + magnitudeDifference);
        int meanDigits = Math.max(0, magnitudeDifference) + 1;


        // for std starting with '1' we allow an extra digit.
        if (std.coefficient < 2 && std.coefficient > 0) {
            //System.out.println("Extra digit");
            meanDigits++;
        }

        //System.out.println("number of relevant digits " + meanDigits + " (" + std + ")");


        // The exponent of the mean is leading, so we simply justify the 'coefficient' of std to
        // match the exponent of mean.
        std.coefficient /= Utils.pow10(magnitudeDifference);


        // For numbers close to 1, we don't use scientific notation.
        if (Math.abs(mean.exponent) < minimumExponent ||
            // neither do we do that if the precission is so high, that we'd show the complete
            // number anyway
            (mean.exponent > 0 && meanDigits > mean.exponent)) {

            double pow = Utils.pow10(mean.exponent);
            mean.exponent = 0;
            mean.coefficient *= pow;
            std.coefficient  *= pow;

        }

        boolean useE = mean.exponent != 0;

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        int fd = meanDigits -  Utils.log10(largeError ? std.coefficient :  mean.coefficient);
        //System.out.println(meanDigits + " -> " + mean + " -> " + fd);
        nf.setMaximumFractionDigits(fd);
        nf.setMinimumFractionDigits(fd);
        nf.setGroupingUsed(false);
        return
            (useE ? "(" : "") + valueAndError(nf.format(mean.coefficient), nf.format(std.coefficient))
            +
            (useE ? (")" + TIMES_10 + TextUtils.superscript(mean.exponent)) : "");
    }

    public static String valueAndError(String value, String error) {
         return value +  " \u00B1 " + error;
    }

    public static String scientificNotation(double meanDouble, int minimumExponent) {
        SplitNumber mean = SplitNumber.split(meanDouble);

        // For numbers close to 1, we don't use scientific notation.
        if (Math.abs(mean.exponent) < minimumExponent) {
            double pow = Utils.pow10(mean.exponent);
            mean.exponent = 0;
            mean.coefficient *= pow;
        }
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(14);
        nf.setGroupingUsed(false);
        boolean useE = mean.exponent != 0;

        return
            nf.format(mean.coefficient)
            +
            (useE ? TIMES_10 + TextUtils.superscript(mean.exponent)  : "");

    }


    public static <T extends Enum<T>> String toString(T[] values, int[] basic) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < basic.length; i++) {
            int b = basic[i];
            if (b != 0) {
                builder.append(values[i].name());
                if (b != 1) {
                    builder.append(TextUtils.superscript(b));
                }
            }
        }
        return builder.toString();
    }

}
