package org.meeuw.math.text;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.numbers.NumberOperations;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation;

import static org.meeuw.math.text.AbstractUncertainFormat.VALUE_FIELD;

/**
 *  Utilities to deal with 'scientific notation', and {@link org.meeuw.math.uncertainnumbers.UncertainNumber uncertaintity}
 *  <p>
 *  For scientific notation {@link #TIMES_10} is used (rather than the 'e' notation).
 * @since 0.19
 */
public class ScientificNotation<N extends Number> {

    public static final String TIMES_10 = TextUtils.TIMES + "10";  /* "·10' */

    protected final IntSupplier minimumExponentSupplier;

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
        this.minimumExponentSupplier = minimumExponent;
        this.maximalPrecisionSupplier = maximalPrecision;
        this.uncertaintyNotationSupplier = uncertaintyNotation;
        this.numberFormatSupplier = numberFormat;
        this.operations = operations;
    }


    private void formatInfinity(StringBuffer buffer, FieldPosition position, N value) {
        buffer.append(operations.signum(value) == 1 ? "+"  : "-");
        buffer.append(TextUtils.INFINITY);
        position.setEndIndex(buffer.length());
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
        formatWithUncertainty(mean, uncertaintity, buffer, position, uncertaintyNotationSupplier.get(), true);
    }

    private void formatWithUncertainty(
        @NonNull N mean,
        @NonNull N uncertainty,
        StringBuffer buffer,
        FieldPosition position,
        UncertaintyConfiguration.Notation uncertaintyNotation,
        boolean errorIndication
        ) {
        if (! operations.isFinite(mean)) {
            formatInfinity(buffer, position, mean);
        } else {
            int minimumExponent  = minimumExponentSupplier.getAsInt();
            int maximalPrecision = maximalPrecisionSupplier.getAsInt();

            SplitNumber<N> splitMean = SplitNumber.split(operations, mean).orElse(new SplitNumber<>(mean, 0));
            SplitNumber<N> splitStd = SplitNumber.split(operations, uncertainty).orElse(null);

            errorIndication &= splitStd != null;

            boolean largeError = errorIndication &&
                operations.gt(
                    uncertainty,
                    operations.abs(mean)
                );

            NumberFormat format = numberFormatSupplier.get();
            final boolean useE;
            if (splitStd != null) {
                arrangeError(
                    splitMean,
                    splitStd,
                    format,
                    minimumExponent,
                    maximalPrecision
                );
                useE = splitMean.exponent != 0;
            } else {
                arrangeErrorForExact(
                    splitMean,
                    format,
                    minimumExponent,
                    operations.precision(splitMean.coefficient)
                );
                useE = Math.abs(splitMean.exponent) > minimumExponent;
            }



            final boolean useBrackets = useE && uncertaintyNotation == Notation.PLUS_MINUS && errorIndication;
            if (useBrackets) {
                buffer.append('(');
            }
            UncertainFormatUtils.valueAndError(
                buffer,
                format,
                position,
                splitMean.coefficient,
                errorIndication ? splitStd.coefficient : null,
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
     * This method has side effector on it's input arguments
     * - The both split numbers will be scaled
     * - the number format will be configured to a fixed number of fraction digits
     *
     */
    void arrangeError(
        SplitNumber<N> splitMean,
        SplitNumber<N> splitStd,
        NumberFormat format,
        int minimumExponent,
        int maximalPrecision) {
        // use difference of order of magnitude of std to determine how mean digits of the mean are
        // relevant
        final int magnitudeDifference = splitMean.exponent - splitStd.exponent;
        //System.out.println("Md: " + mean + " " + std + magnitudeDifference);

        int meanDigits =  magnitudeDifference; // at least one digit

        assert operations.isNaN(splitMean.coefficient) || operations.lt(operations.abs(splitMean.coefficient), 10) : "unexpected coefficient " + splitMean.coefficient;

        // for std starting with '1' we allow an extra digit.
        if (operations.lt(splitStd.coefficient, 2) &&
            operations.signum(splitStd.coefficient) > 0) {
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


        meanDigits = Math.min(meanDigits, maximalPrecision);

        // just pin the number of shown digits
        format.setMaximumFractionDigits(meanDigits);
        format.setMinimumFractionDigits(meanDigits);
    }

     void arrangeErrorForExact(
        SplitNumber<N> splitMean,
        NumberFormat format,
        int minExponent,
        int maximalPrecision
        ) {

        if (Math.abs(splitMean.exponent) < minExponent) {
            splitMean.coefficient = operations.scaleByPowerOfTen(splitMean.coefficient, splitMean.exponent);
            splitMean.exponent = 0;
        }
         // consider exact.
        // just pin the number of shown digits
        format.setMaximumFractionDigits(maximalPrecision);
        format.setMinimumFractionDigits(maximalPrecision);

    }

    /**
     * Format the number without any error indication.
     *
     * @param uncertainty For correct rounding, the uncertainty is still important to know
     *
     * @since 0.19
     */
    protected void format(
        @NonNull N mean,
        @NonNull N uncertainty,
        StringBuffer buffer,
        FieldPosition position) {
        formatWithUncertainty(
            mean,
            uncertainty,
            buffer,
            position,
            uncertaintyNotationSupplier.get(),
            false
        );

    }


    /**
     * Wrapper for {@link #formatWithUncertainty(Number, Number, StringBuffer, FieldPosition, Notation, boolean)}
     */
    public String formatWithUncertainty(
        N mean,
        N uncertainty) {
        StringBuffer result = new StringBuffer();
        formatWithUncertainty(
            mean,
            uncertainty,
            result,
            new FieldPosition(VALUE_FIELD),
            uncertaintyNotationSupplier.get(),
            true
        );
        return result.toString();
    }



    /**
     * Format the number without any error indication.
     * Wrapper for {@link #format(Number, Number, StringBuffer, FieldPosition)}
     */
    public String format(N mean, N uncertainity) {
        StringBuffer  result = new StringBuffer();
        format(
            mean,
            uncertainity,
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
