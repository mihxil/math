/*

This software is OSI Certified Open Source Software.
OSI Certified is a certification mark of the Open Source Initiative.

The license (Mozilla version 1.0) can be read at the MMBase site.
See http://www.MMBase.org/license

*/

package org.meeuw.math;

import java.text.NumberFormat;
import java.util.Locale;

/**
 .
 *
 * @author Michiel Meeuwissen
 */


public abstract class MeasurementNumber<T extends MeasurementNumber<T>> extends Number {


    protected int count = 0;
    protected int minimumExponent = 4;

    public MeasurementNumber() {
    }
    protected MeasurementNumber(int count) {
        this.count = count;
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

    abstract double getStandardDeviation();

    public int getCount() {
        return count;
    }


    /**
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     */
    public void setMinimumExponent(int m) {
        minimumExponent = m;
    }

    public abstract T enter(T m);

    public abstract T div(double d);

    public abstract T times(double d);


    public T combine(T m) {
        return enter(m);
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        double stdDouble = getStandardDeviation();
        double meanDouble = doubleValue();
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
        //System.out.println("me: " + mean);


        boolean useE = mean.exponent != 0;

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        int fd = meanDigits -  Utils.log10(largeError ? std.coefficient :  mean.coefficient);
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
              Utils.superscript(mean.exponent))
             : "");
    }

}



