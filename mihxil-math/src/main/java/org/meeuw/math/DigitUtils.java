package org.meeuw.math;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.*;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.System.arraycopy;
import static org.meeuw.math.ArrayUtils.*;

/**
 * Utilities related to dealing with integer that are represented an array of digits.
 * A digit is normally represented by a {@code byte}
 * <p>
 * This is targeted e.g. at an implementation of p-adic numbers.
 *
 * @see java.math.BigInteger
 * @since 0.14
 */
public class DigitUtils {

    private DigitUtils() {}


    /**
     * Converts a number to given base representation
     * @param basei The base to convert to
     * @param value the value to convert
     * @return a byte-array representing the same number
     */
    public static byte[] toBase(int basei, long value) {
        byte[] result = new byte[100];
        int i = 0;
        while(value > 0) {
            result = ensureCapacity(i, result);
            result[i] = (byte) (value % basei);
            value /= basei;
        }
        byte[] truncated = new byte[i];
        arraycopy(result, 0, truncated, 0, truncated.length);
        return truncated;
    }
    /**
     * Given an array of digits of a number, convert it to a long
     *
     * @param digits where the least significant digit is at {@code 0}
     */
    public static long fromDigitsInBase(final int base, @Min(0) int... digits) {
        long result = 0;
        for (long d : digits) {
            if (d >= base) {
                throw new IllegalArgumentException();
            }
            result = base * result + d ;
        }
        return result;
    }

    public static long fromDigits(@Min(0) @Max(9) int... digits) {
        return fromDigitsInBase(10, digits);
    }

    /**
     * Given an array of digits of a number, convert it to a long
     *
     * @param digits where the least significant digit is at {@code digits[0]}
     */
    public static long fromInverseDigitsInBase(final byte base, @Min(0) byte... digits) {
        long result = 0;
        long pow = 1;
        for (byte d : digits) {
            if (d >= base) {
                throw new IllegalArgumentException();
            }
            result += pow * toUnsignedInt(d);
            pow *= toUnsignedInt(base);
        }
        return result;

    }

    public static byte[] multiplyInverseDigitsWithCarry(int basei, byte digit, byte[] multiplicand) {
        byte[] result = new byte[multiplicand.length + 1];
        int carry = 0;
        for (int i = 0 ; i < multiplicand.length;i ++) {
            int ri = carry ;
            ri += toUnsignedInt(digit) * toUnsignedInt(multiplicand[i]);
            result[i] = (byte) (ri % basei);
            carry = ri / basei;
        }
        result[result.length - 1] = (byte) (carry);
        return result;
    }


    public static byte[] multiplyInverseDigits(int basei, byte digit, byte[] multiplicand) {
        byte[] result = multiplyInverseDigitsWithCarry(basei, digit, multiplicand);
        return removeTrailingZeros(result);
    }


    /**
     * Performs 'long multiplication' on two numbers represented by a {@code byte[]}, where the least significant digit is the one at {@code [0]}
     * @param base  The base of this number
     * @see #multiplyInverseDigits(int, byte, byte[])
     * @see #sumInverseDigits(int, byte[]...)
     */
    public static byte[] multiplyInverseDigits(int base, byte[] multiplicand1, byte[] multiplicand2) {

        // swap if necessary.
        if (multiplicand2.length < multiplicand1.length) {
            byte[] swap = multiplicand1;
            multiplicand1 = multiplicand2;
            multiplicand2 = swap;
        }
        byte[][] result = new byte[multiplicand1.length][];
        for (int i = 0; i < multiplicand1.length; i++) {
            result[i] = multiplyInverseDigits(base, multiplicand1[i], multiplicand2);
            if (i != 0) {
                byte[] newResult = new byte[result[i].length + i];
                arraycopy(result[i], 0, newResult, i, result[i].length);
                result[i] = newResult;
            }
        }
        return sumInverseDigits(base, result);
    }


    /**
     * Performs a sum of number of integers
     * @param basei  The base of this number
     */
    public static byte[] sumInverseDigits(int basei, byte[]... a) {
        int max = maxLength(a);
        int carry = 0;
        byte[] result = new byte[max];
        for (int i = 0 ; i < max;i ++) {
            int ri = carry ;
            for (byte[] b : a) {
                ri += toUnsignedInt(digit(i, b));
            }
            result[i] = (byte) (ri % basei);
            carry = ri / basei;
        }
        while (carry != 0) {
            byte[] newResult = new byte[result.length + 1];
            arraycopy(result, 0, newResult, 0, result.length);
            result = newResult;
            result[result.length - 1] = (byte) (carry % basei);
            carry /= basei;
        }
        return result;
    }



    public static byte[] ensureCapacity(int i, byte[] bytes) {
        if (bytes.length > i) {
            return bytes;
        } else {
            byte[] newBytes = new byte[bytes.length + 100];
            arraycopy(bytes, 0, newBytes, 0, bytes.length);
            return newBytes;
        }
    }


    static int maxLength(byte[]... values) {
        int result = 0;
        for (byte[] v : values) {
            if (v.length > result) {
                result = v.length;
            }
        }
        return result;
    }

    static byte digit(int n, byte[] digits) {
        return digits.length > n ? digits[n] : 0;
    }

    static byte repetitiveDigit(int n, byte[] digits) {
        return digits[n % digits.length];
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    public static class CarryAndIndex {
        final int carry;
        final int index;

        @Override
        public String toString() {
            return index + " carry " + carry;
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    public static class MultiplierAndNewDigitAndCarry {
        final int index;
        final byte multiplier;
        final byte newDigit;
        final byte carry;

        @Override
        public String toString() {
            return index + "(" + toUnsignedInt(multiplier) + ")->" + toUnsignedInt(newDigit) + " (carry " + carry + ")";
        }
    }

    @AllArgsConstructor
    public static class CarryAndIndices {
        final int carry;
        final int[] indices;
        @Override
        public String toString() {
            return carry + "," + ArrayUtils.toString(indices);
        }

      @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass", "EqualsDoesntCheckParameterClass"})
        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            CarryAndIndices that = (CarryAndIndices) o ;
            return carry == that.carry && Arrays.equals(indices, that.indices);
        }

        @Override
        public int hashCode() {
            int result = carry;
            result = 31 * result + Arrays.hashCode(indices);
            return result;
        }
    }

    public static String digitToString(byte b) {
        int i = toUnsignedInt(b);
        if (i < 10) {
            return Byte.toString(b);
        }
        char codePoint = (char) ('a' + i - 10);
        return String.valueOf(codePoint);
    }


    /**
     * We store digits as {@code byte} arrays with the least relevant digit at position
     * {@code 0} ('inverse digits'). The string representation has the least relevant digit
     * on the most right position..
     */
    public static byte[] stringToInverseDigits(String s) {
        byte[] result = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(s.length() - 1 - i);
            if (c >= '0' && c <= '9') {
                result[i] = (byte) (c - '0');
            } else {
                result[i] = (byte) (c - 'a' + 10);
            }
        }
        return result;
    }



}
