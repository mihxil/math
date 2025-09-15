package org.meeuw.math.text;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.meeuw.math.numbers.NumberOperations;
import org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation;

import static org.meeuw.math.text.AbstractUncertainFormat.VALUE_FIELD;
import static org.meeuw.math.text.UncertainFormatUtils.EXACT_DOUBLE_FORMAT;

/**
 *  Utilities to deal with 'scientific notation', and {@link org.meeuw.math.uncertainnumbers.UncertainNumber uncertaintity}
 *  <p>
 *  For scientific notation {@link #TIMES_10} is used (rather than the 'e' notation).
 * @since 0.19
 */
public class ScientificNotation<N extends Number> {

    public static final String TIMES_10 = TextUtils.TIMES + "10";  /* "·10' */

    protected final IntSupplier minimumExponenSupplier;

    protected final IntSupplier maximalPrecisionSupplier;

    protected final Supplier<Notation> uncertaintyNotationSupplier;

    protected final Supplier<NumberFormat> numberFormatSupplier;

    protected final NumberOperations<N> operations;


    public ScientificNotation(AbstractUncertainFormat<?, ?, N> formatter, NumberOperations<N> operations) {
         this(
             formatter::getMinimumExponent,
             formatter::getMaximalPrecision,
             formatter::getUncertaintyNotation,
             () -> (NumberFormat) formatter.getNumberFormat().clone(),
                operations
         );
    }

    public ScientificNotation(
        IntSupplier minimumExponent,
        IntSupplier maximalPrecision,
        Supplier<Notation> uncertaintyNotation,
        Supplier<NumberFormat> numberFormat,
        NumberOperations<N> operations
        ) {
        this.minimumExponenSupplier = minimumExponent;
        this.maximalPrecisionSupplier = maximalPrecision;
        this.uncertaintyNotationSupplier = uncertaintyNotation;
        this.numberFormatSupplier = numberFormat;
        this.operations = operations;
    }


    /**
     * Represents the mean value in a scientific notation (using Unicode characters), if sensible.
     * The value of the standard deviation is used to determine how many digits can sensibly be shown.
     * <p>
     * If the string will not be less concise without using scientific notation, it may do that. E.g. if the value is very precise, you can just as wel just state all known digits, and the string-notation will not be less consise or suggesting more precision than it has.
     * <p>

     * @since 0.19
     * @param mean The value to represent
     * @param uncertaintity The uncertaintity in that value
     * @param buffer The buffer to append the result to.
     * @param position this is used in {@link java.text.Format} implementations
     */
    public void formatWithUncertainty(
        N mean,
        N uncertaintity,
        StringBuffer buffer,
        FieldPosition position) {
        if (! operations.isFinite(mean)) {
            EXACT_DOUBLE_FORMAT.get().format(
                mean,
                buffer,
                position
            );
        } else {
            int minimumExponent = minimumExponenSupplier.getAsInt();
            int maximalPrecision = maximalPrecisionSupplier.getAsInt();
            Notation uncertaintyNotation = uncertaintyNotationSupplier.get();

            SplitNumber<N> splitMean = SplitNumber.split(operations, mean);
            SplitNumber<N> splitStd = SplitNumber.split(operations, uncertaintity);

            boolean largeError = operations.gt(operations.abs(uncertaintity), operations.abs(mean));

            // use difference of order of magnitude of std to determine how mean digits of the mean are
            // relevant
            int magnitudeDifference = splitMean.exponent - splitStd.exponent;
            //System.out.println("Md: " + mean + " " + std + magnitudeDifference);

            int meanDigits = magnitudeDifference; // at least one digit

            assert operations.isNaN(splitMean.coefficient) || operations.lt(operations.abs(splitMean.coefficient), 10) : "unexpected coefficient " + splitMean.coefficient;

            // for std starting with '1' we allow an extra digit.
            if (operations.lt(splitStd.coefficient, 2) && operations.signum(splitStd.coefficient) > 0) {
                //System.out.println("Extra digit");
                meanDigits++;
            }

            //System.out.println("number of relevant digits " + meanDigits + " (" + std + ")");


            // The exponent of the mean is leading, so we simply justify the 'coefficient' of std to
            // match the exponent of mean.
            splitStd.coefficient = operations.scaleByPowerOfTen(
                splitStd.coefficient,
                -1 * magnitudeDifference
            );
            splitStd.exponent += magnitudeDifference;


            // For numbers close to 1, we don't use scientific notation.
            if (splitMean.exponent != 0 // no useE anyways
                && (
                Math.abs(splitMean.exponent) < minimumExponent ||
                // neither do we do that if the precision is so high, that we'd show the complete
                // number anyway
                    (meanDigits >= Math.abs(splitMean.exponent))
            )
            ) {

                meanDigits -= splitMean.exponent;

                splitMean.coefficient = operations.scaleByPowerOfTen(splitMean.coefficient, splitMean.exponent);
                splitStd.coefficient = operations.scaleByPowerOfTen(splitStd.coefficient, splitMean.exponent);
                splitMean.exponent = 0;

            }

            boolean useE = splitMean.exponent != 0;

            meanDigits = Math.min(meanDigits, maximalPrecision);

            NumberFormat format = numberFormatSupplier.get();
            format.setMaximumFractionDigits(meanDigits);
            format.setMinimumFractionDigits(meanDigits);

            final boolean useBrackets = useE && uncertaintyNotation == Notation.PLUS_MINUS;
            if (useBrackets) {
                buffer.append('(');
            }
            UncertainFormatUtils.valueAndError(
                buffer,
                format,
                position,
                splitMean.coefficient,
                splitStd.coefficient,
                uncertaintyNotation
            );

            if (useBrackets) {
                buffer.append(')');
            }
            if (useE) {
                buffer.append(TIMES_10);
                TextUtils.superscript(buffer, splitMean.exponent);
            }
        }
    }

     /**
     * Format the number without any error indication.
     *
     * @param minimumExponent If the number is smaller than 10^{@code minimumExponent} no 10^ notation is used.
     * @since 0.19
     */
    public void format(
        N mean,
        int minimumExponent,
        StringBuffer buffer, FieldPosition position) {

        if (! operations.isFinite(mean)) {
            EXACT_DOUBLE_FORMAT.get().format(mean, buffer, position);
        } else {
            SplitNumber<N> splitMean = SplitNumber.split(mean);
            // For numbers close to 1, we don't use scientific notation.
            if (Math.abs(splitMean.exponent) < minimumExponent) {
                splitMean.coefficient = operations.scaleByPowerOfTen(splitMean.coefficient, splitMean.exponent);
                splitMean.exponent = 0;
            }

            boolean useE = splitMean.exponent != 0;
            EXACT_DOUBLE_FORMAT.get().format(splitMean.coefficient, buffer, position);
            if (useE) {
                buffer.append(TIMES_10);
                String superscript = TextUtils.superscript(splitMean.exponent);
                buffer.append(superscript);
                position.setEndIndex(position.getEndIndex() + TIMES_10.length() + superscript.length());
            }
        }
    }


    /**
     * Wrapper for {@link #formatWithUncertainty(Number, Number, StringBuffer, FieldPosition)}
     */
    public String formatWithUncertainty(
        N mean,
        N uncertaintiy) {
        StringBuffer result = new StringBuffer();
        formatWithUncertainty(
            mean,
            uncertaintiy,
            result,
            new FieldPosition(VALUE_FIELD)
        );
        return result.toString();
    }



    /**
     * Format the number without any error indication.
     * Wrapper for {@link #format(Number, int, StringBuffer, FieldPosition)}
     */
    public String format(N mean, int minimumExponent) {
        StringBuffer  result = new StringBuffer();
        format(
            mean,
            minimumExponent,
            result,
            new FieldPosition(VALUE_FIELD)
        );
        return result.toString();
    }

    public N fromString(String number) {
        number = number.replaceFirst(TIMES_10, "E");
        int e = number.indexOf("E");
        if (e >= 0) {
            number = TextUtils.unsuperscript(number, e);
        }
        return operations.fromString(number);
    }
}
