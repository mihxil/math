/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.math;
import java.text.*;
import java.util.*;

/**
 * Represents a set of measurement values. The value represents the average value.
 * {@link #toString} presnent the current value, but only the relevant digits. The standard
 * deviation {@link #getStandardDeviation} is used to determin what digits are relevant.
 *
 * @author Michiel Meeuwissen
 */


public class Measurement extends java.lang.Number {


    private double sum = 0;
    private double squareSum = 0;
    private int count = 0;
    private int minimumExponent = 4;

    public Measurement() {
    }
    protected Measurement(double sum, double squareSum, int count) {
        this.sum = sum;
        this.squareSum = squareSum;
        this.count = count;
    }

    /**
     * Enters new value(s).
     */
    public Measurement enter(double... ds) {
        for(double d : ds) {
            sum += d;
            squareSum += d * d;
            count++;
        }
        return this;
    }

    /**
     * Assuming that the measurement <code>m</code> is from the same set, add it to the already existing
     * statistics.
     * See also {@link #add(Measurement)} which is something entirely different.
     * @param m
     */
    public Measurement enter(Measurement m) {
        sum += m.sum;
        squareSum += m.squareSum;
        count += m.count;
        return this;
    }

    public double getMean() {
        return sum / count;
    }


    @Override
    public double doubleValue() {
        return getMean();
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }
    @Override
    public int intValue() {
        return (int) doubleValue();
    }
    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public byte byteValue() {
        return (byte) doubleValue();
    }
    @Override
    public short shortValue() {
        return (short) doubleValue();
    }

    public double getStandardDeviation() {
        double mean = getMean();
        return Math.sqrt(squareSum / count - mean * mean);
    }

    public int getCount() {
        return count;
    }

    public double getSum() {
        return sum;
    }
    public double getSumOfSquares() {
        return squareSum;
    }

    /**
     * Operator overloading would be very handy here, but java sucks.
     */
    public Measurement div(double d) {
        return new Measurement(sum / d, squareSum / (d * d), count);
    }
    public Measurement times(double d) {
        return new Measurement(sum * d, squareSum * (d * d), count);
    }

    public Measurement add(double d) {
        return new Measurement(sum + d * count, squareSum + d * d * count + 2 * sum * d, count);
    }

    /**
     * Assuming that this measurement is from a different set (the mean is <em>principally
     * different</em>)
     * @todo Not yet correctly implemented
     */
    public Measurement add(Measurement m) {
        // think about this...
        return new Measurement(m.count * sum + count + m.sum, /* er */ 0, count * m.count);
    }


    /**
     * Returns 10 to the power i, a utility in java.lang.Math for that lacks.
     */
    static double pow10(int i) {
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
     * Returns an integer in 'superscript' notation, using unicode.
     */
    private static String superscript(int i) {
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
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     */
    public void setMinimumExponent(int m) {
        minimumExponent = m;
    }


    /**
     * Split a double up in 2 numbers: a double approximately 1 (the 'coefficent'), and an integer
     * indicating the order of magnitude (the 'exponent').
     */
    static class SplitNumber {
        public double coefficient;
        public int   exponent;
        public SplitNumber(double in) {
            boolean negative = in < 0;
            coefficient = Math.abs(in);
            exponent    = 0;
            while (coefficient > 10) {
                coefficient /=10;
                exponent++;
            }
            while (coefficient > 0 && coefficient < 1) {
                coefficient *=10;
                exponent--;
            }
            if (negative) coefficient *= -1;
        }
        @Override
        public String toString() {
            return coefficient + "\u00B710" + superscript(exponent);
            //return coefficient + "E" + exponent;
        }

    }


    /**
     * A crude order of magnitude implemention
     */
    private int log10(double d) {
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
    @Override
    public String toString() {
        double stdDouble = getStandardDeviation();
        double meanDouble = getMean();
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
        std.coefficient /= pow10(magnitudeDifference);


        // For numbers close to 1, we don't use scientific notation.
        if (Math.abs(mean.exponent) < minimumExponent ||
            // neither do we do that if the precission is so high, that we'd show the complete
            // number anyway
            (mean.exponent > 0 && meanDigits > mean.exponent)) {

            double pow = pow10(mean.exponent);
            mean.exponent = 0;
            mean.coefficient *= pow;
            std.coefficient  *= pow;

        }
        //System.out.println("me: " + mean);


        boolean useE = mean.exponent != 0;

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        int fd = meanDigits -  log10(largeError ? std.coefficient :  mean.coefficient);
        //System.out.println(meanDigits + " -> " + mean + " -> " + fd);
        nf.setMaximumFractionDigits(fd);
        nf.setMinimumFractionDigits(fd);
        nf.setGroupingUsed(false);
        return
            (useE ? "(" : "") +
            nf.format(mean.coefficient) +
            (count > 1 ? (
                          " \u00B1 " + /* +/- */
                          nf.format(std.coefficient)
                          ) : "" )  +
            (useE ?
             (")\u00B710" + /* .10 */
              superscript(mean.exponent))
             : "");
    }

}



