package org.meeuw.math.abstractalgebra.padic.impl;

import jakarta.validation.constraints.Min;

import java.math.BigInteger;
import java.util.*;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.exceptions.InvalidElementException;
import org.meeuw.math.text.TextUtils;

import static java.lang.Byte.toUnsignedInt;

/**
 * Utilities for {@link AdicDigits}.
 * <p>
 * An adic number with (eventually) repeating digits is exactly a rational number (with a denominator that is coprime to the base): if the repetend {@code R} has
 * {@code k} digits, and the non-repeating digits {@code D} are {@code n} digits, then the value is
 * <pre>{@code
 *   D + bⁿ · R · (1 + bᵏ + b²ᵏ + ...) = D + bⁿ · R / (1 - bᵏ)
 * }</pre>
 * Conversely, the digits of a rational number can be calculated one by one, and they must eventually repeat (see {@link #toAdicDigits(int, RationalNumber, int)}).
 * <p>
 * So arithmetic is done on the rational values, which is simple and exact. The digits are only needed to represent the result.
 */
public class AdicDigitUtils {

    private AdicDigitUtils() {
        // no instances
    }

    /**
     * The value of the given digits as a rational number.
     *
     * @param base the base to interpret the digits in
     */
    public static RationalNumber toRational(@Min(2) int base, AdicDigits digits) {
        final BigInteger b = BigInteger.valueOf(base);
        final BigInteger d = value(b, digits.digits);
        if (!digits.isRepetitive()) {
            return RationalNumber.of(d);
        }
        final BigInteger r = value(b, digits.repetend);
        final BigInteger oneMinusBk = BigInteger.ONE.subtract(b.pow(digits.repetend.length));
        // D + bⁿ · R / (1 - bᵏ)
        return RationalNumber.of(
            d.multiply(oneMinusBk).add(b.pow(digits.digits.length).multiply(r)),
            oneMinusBk
        );
    }

    /**
     * The value of the digits (with the least significant digit at {@code [0]})
     */
    static BigInteger value(BigInteger base, byte[] inverseDigits) {
        BigInteger result = BigInteger.ZERO;
        for (int i = inverseDigits.length - 1; i >= 0; i--) {
            result = result.multiply(base).add(BigInteger.valueOf(toUnsignedInt(inverseDigits[i])));
        }
        return result;
    }

    /**
     * Calculates the adic digits of a rational number. Digit after digit: the least significant digit {@code d} of {@code a/b} is the one for which
     * {@code a - d·b} is divisible by the base. The next digits are those of {@code (a - d·b)/base / b}. The denominator stays the same, and the numerators
     * end up in a bounded range, so they must repeat at some point. From then on, the digits repeat too.
     * <p>
     * The length of the repetend can be big though (e.g. {@code 1/1000003} in base {@code 5} repeats only after {@code 500001} digits), so a maximum must be given.
     *
     * @param base the base
     * @param value the value. Its denominator must be coprime to the base.
     * @param maximalNumberOfDigits the maximal number of digits to calculate (non-repeating and repeating together)
     * @return the digits, or {@link Optional#empty()} if the digits didn't repeat within the given number of digits
     * @throws InvalidElementException if the denominator is not coprime to the base (then the number has digits after the point)
     */
    public static Optional<AdicDigits> toAdicDigits(@Min(2) int base, RationalNumber value, int maximalNumberOfDigits) {
        final Expansion expansion = expand(base, value, maximalNumberOfDigits);
        if (expansion.repetendStart < 0) {
            return Optional.empty();
        }
        final byte[] digits = Arrays.copyOfRange(expansion.digits, 0, expansion.repetendStart);
        final byte[] repetend = Arrays.copyOfRange(expansion.digits, expansion.repetendStart, expansion.digits.length);
        return Optional.of(AdicDigits.create(repetend, digits));
    }

    /**
     * As {@link #toAdicDigits(int, RationalNumber, int)}, but just the given number of least significant digits, and no attempt to find the repetend.
     * @return the digits, with the least significant one at {@code [0]}
     */
    public static byte[] leastSignificantDigits(@Min(2) int base, RationalNumber value, int numberOfDigits) {
        final BigInteger b = BigInteger.valueOf(base);
        final BigInteger denominator = value.getDenominator();
        final BigInteger inverse = inverseOfDenominator(b, value);
        BigInteger numerator = value.getNumerator();
        final byte[] result = new byte[numberOfDigits];
        for (int i = 0; i < numberOfDigits; i++) {
            final BigInteger digit = numerator.multiply(inverse).mod(b);
            result[i] = digit.byteValue();
            numerator = numerator.subtract(digit.multiply(denominator)).divide(b);
        }
        return result;
    }

    private record Expansion(byte[] digits, int repetendStart) {
    }

    private static Expansion expand(@Min(2) int base, RationalNumber value, int maximalNumberOfDigits) {
        final BigInteger b = BigInteger.valueOf(base);
        final BigInteger denominator = value.getDenominator();
        final BigInteger inverse = inverseOfDenominator(b, value);
        final Map<BigInteger, Integer> seen = new HashMap<>();
        final byte[] digits = new byte[maximalNumberOfDigits];
        BigInteger numerator = value.getNumerator();
        for (int i = 0; i < maximalNumberOfDigits; i++) {
            final Integer previous = seen.putIfAbsent(numerator, i);
            if (previous != null) {
                return new Expansion(Arrays.copyOf(digits, i), previous);
            }
            final BigInteger digit = numerator.multiply(inverse).mod(b);
            digits[i] = digit.byteValue();
            numerator = numerator.subtract(digit.multiply(denominator)).divide(b);
        }
        return new Expansion(digits, -1);
    }

    private static BigInteger inverseOfDenominator(BigInteger base, RationalNumber value) {
        try {
            return value.getDenominator().modInverse(base);
        } catch (ArithmeticException ae) {
            throw new InvalidElementException(value + " has no adic digits in base " + base + ", since its denominator is not coprime to it");
        }
    }

    private static final int MAXIMAL_NUMBER_OF_DIGITS = 100_000;

    private static AdicDigits fromRational(int base, RationalNumber value) {
        return toAdicDigits(base, value, MAXIMAL_NUMBER_OF_DIGITS)
            .orElseThrow(() -> new ArithmeticException("The digits of " + value + " don't repeat within " + MAXIMAL_NUMBER_OF_DIGITS + " digits"));
    }

    /**
     * Multiplication of adic numbers.
     * @param base The base to interpret the digits for
     */
    public static AdicDigits multiplyAdicDigits(
        @Min(2) final int base,
        final AdicDigits multiplier,
        final AdicDigits multiplicand) {
        return fromRational(base, toRational(base, multiplier).times(toRational(base, multiplicand)));
    }

    /**
     * Multiply an {@link AdicDigits} with exactly one digit.
     * @param multiplier The multiplier digit
     * @param multiplicand The multiplicand {@link AdicDigits}
     */
    public static AdicDigits multiplyAdicDigits(
        @Min(2) final int base,
        final byte multiplier,
        final AdicDigits multiplicand) {
        return fromRational(base, toRational(base, multiplicand).times(BigInteger.valueOf(toUnsignedInt(multiplier))));
    }

    /**
     * The sum of a number of adic numbers
     * @param base  The base of the numbers
     */
    public static AdicDigits sumAdicDigits(@Min(2) int base, AdicDigits... summands) {
        RationalNumber sum = RationalNumber.ZERO;
        for (AdicDigits summand : summands) {
            sum = sum.plus(toRational(base, summand));
        }
        return fromRational(base, sum);
    }

    public static AdicDigits negate(@Min(2) int base, AdicDigits digits) {
        return fromRational(base, toRational(base, digits).negation());
    }

    public static String adicToString(@Min(2) int base, AdicDigits digits) {
        return digits.toString() + TextUtils.subscript(base);
    }
}
