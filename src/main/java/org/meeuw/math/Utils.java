package org.meeuw.math;

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

}
