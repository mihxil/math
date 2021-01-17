package org.meeuw.math.text;

import lombok.*;

import java.text.*;
import java.util.Locale;

import org.meeuw.math.Utils;
import org.meeuw.math.text.spi.Configuration;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainDoubleFormat extends Format {

    public static final String TIMES = "\u00B7";  /* "·10' */
    public static final String PLUSMIN = "\u00B1";
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

    @Getter
    @Setter
    private Configuration.UncertaintyNotation uncertaintyNotation = Configuration.UncertaintyNotation.PLUS_MINUS;

    @Getter
    @Setter
    private NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

    @Getter
    @Setter
    private double considerRoundingErrorFactor = 1000d;


    private boolean roundingErrorsOnly(double value, double uncertainty) {
        return uncertainty < uncertaintyForDouble(value) * considerRoundingErrorFactor;
    }

    @Override
    public StringBuffer format(Object number, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        if (number instanceof UncertainDouble) {
            UncertainDouble<?> uncertainNumber = (UncertainDouble<?>) number;
            if (uncertainNumber.isExact() || roundingErrorsOnly(uncertainNumber.getValue(), uncertainNumber.getUncertainty())) {
                toAppendTo.append(scientificNotation(uncertainNumber.getValue(), minimumExponent));
            } else {
                toAppendTo.append(scientificNotationWithUncertainty(uncertainNumber.getValue(), uncertainNumber.getUncertainty()));
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
    String scientificNotationWithUncertainty(
        double meanDouble,
        double stdDouble) {
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
            // neither do we do that if the precision is so high, that we'd show the complete
            // number anyway
            (mean.exponent > 0 && meanDigits > mean.exponent)) {

            double pow = Utils.pow10(mean.exponent);
            mean.exponent = 0;
            mean.coefficient *= pow;
            std.coefficient  *= pow;

        }

        boolean useE = mean.exponent != 0;

        int fd = meanDigits -  Utils.log10(largeError ? std.coefficient :  mean.coefficient) - 1;
        //System.out.println(meanDigits + " -> " + mean + " -> " + fd);
        NumberFormat format = (NumberFormat) numberFormat.clone();
        format.setMaximumFractionDigits(fd);
        format.setMinimumFractionDigits(fd);
        format.setGroupingUsed(false);
        final boolean useBrackets = useE && uncertaintyNotation == Configuration.UncertaintyNotation.PLUS_MINUS;
        return
            (useBrackets ? "(" : "") + valueAndError(format.format(mean.coefficient), format.format(std.coefficient), uncertaintyNotation)
            +
            (useBrackets ? ")" : "") + (useE ? (TIMES_10 + TextUtils.superscript(mean.exponent)) : "");
    }

    public static String valuePlusMinError(String value, String error) {
        return value + ' ' + PLUSMIN + ' ' + error;
    }

    public static String valueParenthesesError(String value, String error) {
        int i = 0;
        while (i < error.length() && (error.charAt(i) == '0' || error.charAt(i) == '.')) {
             i++;
         }
         return value +  "(" + error.substring(i) + ")";
    }

    public static String valueAndError(String value, String error, Configuration.UncertaintyNotation uncertaintyNotation) {
         switch(uncertaintyNotation) {

             case PARENTHESES: return valueParenthesesError(value, error);
             default:
             case PLUS_MINUS: return valuePlusMinError(value, error);
         }
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


}
