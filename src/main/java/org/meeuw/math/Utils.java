package org.meeuw.math;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class Utils {

    /**
     * Returns an integer in 'superscript' notation, using unicode.
     */
    public static String superscript(int i) {
        StringBuilder bul = new StringBuilder();
        boolean minus = false;
        if (i < 0) {
            minus = true;
            i = -1 * i;
        }
        if (i == 0) {
            bul.insert(0, Character.toChars(0x2070));
        }
        while (i > 0) {
            int j = i % 10;
            i /= 10;
            bul.insert(0, Character.toChars(0x2070 + j)[0]);
        }
        if (minus) bul.insert(0, "\u207B");

        return bul.toString();

    }

    /**
     * Returns 10 to the power i, a utility in java.lang.Math for that lacks.
     */
    public static double pow10(int i) {
        double result = 1;
        while (i > 0) {
            result *= 10;
            i--;
        }
        while (i < 0) {
            result /= 10;
            i++;
        }
        assert i == 0;
        return result;
    }

     /**
     * Returns 10 to the power i, a utility in java.lang.Math for that lacks.
     */
    public static long positivePow10( int i) {
        long result = 1;
        while (i > 0) {
            result *= 10;
            i--;
        }
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        assert i == 0;
        return result;
    }

     /**
     * A crude order of magnitude implemention
     */
    public static int log10(double d) {
        d = Math.abs(d);
        int result = 0;
        while (d >= 1) {
            d /= 10;
            result++;
        }
        while (d > 0 && d < 0.1) {
            d *= 10;
            result--;
        }
        return result;
    }

     /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
     public static String scientificNotation(double meanDouble, double stdDouble, int minimumExponent) {
        SplitNumber std  = new SplitNumber(stdDouble);
        SplitNumber mean = new SplitNumber(meanDouble);
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
            (useE ? "(" : "") +
                valueAndError(nf.format(mean.coefficient), nf.format(std.coefficient))

            +
            (useE ?
             (")\u00B710" + /* .10 */
              Utils.superscript(mean.exponent))
             : "");
    }

    public static String valueAndError(String value, String error) {
         return value +  " \u00B1 " + error;
    }

}
