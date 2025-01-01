package org.meeuw.math;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.*;

import org.meeuw.math.text.TextUtils;

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

    static byte[] multiplyInverseDigitsWithCarry(int basei, byte digit, byte[] multiplicand) {
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
     * Multiply an {@link AdicDigits} with exactly one digit.
     * @param multiplier The multiplier digit
     * @param multiplicand The multiplicand {@link AdicDigits}
     */
    public static AdicDigits multiplyAdicDigits(
        final int base,
        final  byte multiplier,
        final AdicDigits multiplicand) {
        final byte[] resultDigits = multiplyInverseDigitsWithCarry(base, multiplier, multiplicand.digits);
        final int originalCarry = toUnsignedInt(resultDigits[resultDigits.length - 1]);

        if (multiplicand.repetend.length == 0 || Arrays.equals(multiplicand.repetend, AdicDigits.NOT_REPETITIVE)) {
            return AdicDigits.create(
                AdicDigits.NOT_REPETITIVE,
                resultDigits);
        }
        int carry = originalCarry;
        int digiti = toUnsignedInt(multiplier);
        List<CarryAndIndex> carries = new ArrayList<>();
        List<Byte> moreDigits = new ArrayList<>();
        int i = 0;
        byte[] repetitive;
        while(true) {
            int index = i % multiplicand.repetend.length;
            CarryAndIndex carryAndIndex = new CarryAndIndex(carry, index);
            int indexOf = carries.indexOf(carryAndIndex);
            if (indexOf == -1) {
                carries.add(carryAndIndex);
                int ri = carry +  digiti * toUnsignedInt(multiplicand.repetend[index]);
                byte b = (byte) (ri % base);
                moreDigits.add(Byte.valueOf(b));
                carry = ri / base;
                i++;
            } else {
                repetitive = new byte[moreDigits.size() - indexOf];
                for (int j= 0; j < repetitive.length; j++) {
                    repetitive[j] = moreDigits.remove(indexOf);;
                }
                break;
            }
        }
        int numberOfOriginalDigits =  Math.max(0, resultDigits.length - 1);
        int numberOfDigits = Math.max(0, moreDigits.size()  + numberOfOriginalDigits);
        byte[] digits = new byte[numberOfDigits];

        arraycopy(resultDigits, 0, digits, 0, numberOfDigits);
         for (int j = 0; j < moreDigits.size(); j++) {
            digits[numberOfOriginalDigits + j] = moreDigits.get(j);
        }
        return AdicDigits.create(repetitive, digits);
     }


    /**
     * Long multiplication of adic numbers. With proper propagation of the repetitive part.
     * @param base The base to interpret the digitis for
     * @param multiplier
     * @param multiplicand
     */
    public static AdicDigits multiplyAdicDigits(
        final int base,
        final AdicDigits multiplier,
        final AdicDigits multiplicand) {
        int i = -1;
        AdicDigits sum = null;
        List<MultiplierAndNewDigitAndCarry> list = null; // only relevant when repeating digits.
        while(true) {
            i++;
            AdicDigits.ByteAndIndex multiplierDigit = multiplier.get(i).orElse(null);
            if (multiplierDigit == null) {
                return sum;
            }
            if (multiplierDigit.value != 0) {

                var shiftedMultiplicand = multiplicand.leftShift(i);
                final AdicDigits summand = multiplyAdicDigits(base,
                    multiplierDigit.value,
                    shiftedMultiplicand
                );
                if (sum == null) {
                    sum = summand;
                } else {
                    sum = sumAdicDigits(base, sum, summand);
                }
            }


            if (multiplierDigit.repeating) {
                AdicDigits.ByteAndIndex newDigit = sum.get(i).orElseThrow(() -> new IllegalStateException());
                if (list == null) {
                     list = new ArrayList<>();
                }
                byte carry = sum.repetend[0];
                MultiplierAndNewDigitAndCarry l = new MultiplierAndNewDigitAndCarry(multiplierDigit.index, multiplierDigit.value, newDigit.value, carry);
                int indexOf = list.indexOf(l);
                if (indexOf >= 0) {
                //if (i > 100) {
                    System.out.println(sum + "\nAlready saw " + l);

                    // so we know the length of the repetitive bit.
                    byte[] repetitive = new byte[indexOf + 1];

                    arraycopy(sum.digits ,  sum.digits.length - repetitive.length, repetitive, 0,  repetitive.length);

                    // what remains are the normal digits.
                    int digitLength = sum.digits.length - repetitive.length;

                    byte[] digits = new byte[digitLength];
                    arraycopy(sum.digits, 0, digits, 0, digits.length);
                    return AdicDigits.create(repetitive, digits);
                } else {
                    //System.out.println(i + " new " + l + " (" +sum + "->" + sum + ")");
                    list.add(0, l);
                }
            }
        }

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


    public static AdicDigits sumAdicDigits(int base, AdicDigits... summands) {
        return sumAdicDigits((byte) base, summands);
    }

    /**
     * Performs a sum of number of integers
     * @param base  The base of this number
     */
    public static AdicDigits sumAdicDigits(byte base, AdicDigits... summands) {
        byte[] result = new byte[100];
        int carry = 0;
        int i = 0;
        List<CarryAndIndices> detecting = new ArrayList<>();
        while(true) {
            int r = carry;
            boolean detectRepetition = true;
            boolean anyPresent = false;
            for (AdicDigits summand : summands) {

                Optional<AdicDigits.ByteAndIndex> byteAndIndex = summand.get(i);
                if (byteAndIndex.isPresent()) {
                    anyPresent = true;
                    detectRepetition &= byteAndIndex.get().repeating;
                    r += byteAndIndex.get().value;
                }
            }
            if (! anyPresent) {
                break;
            }
            result = ensureCapacity(i, result);
            result[i] = (byte) (r % base);
            i++;
            if (detectRepetition) {
                CarryAndIndices check = new CarryAndIndices(
                    carry,
                    AdicDigits.getIndexes(i, summands)
                );
                int indexOf = detecting.indexOf(check);
                if (indexOf == -1) {
                    detecting.add(check);
                } else {
                    i--;
                    while(indexOf-- > 0) {
                        detecting.remove(0);
                    }
                    break;
                }
            }
            carry = r / base;
        }
        byte[] digits = new byte[i - detecting.size()];
        byte[] repetitive = new byte[detecting.size()];
        arraycopy(result, 0, digits, 0, digits.length);
        arraycopy(result, i - detecting.size(), repetitive, 0, repetitive.length);
        if (repetitive.length == 0) {
            repetitive = AdicDigits.NOT_REPETITIVE;
        }
        return new AdicDigits(repetitive, digits);
    }

    public static AdicDigits negate(byte base, AdicDigits negated) {
        byte[] negatedDigits = new byte[negated.digits.length];
        byte[] negatedRepetend = new byte[negated.repetend.length];
        byte carry = 0;
        for (int i = 0 ; i < negated.digits.length; i++) {
            negatedDigits[i] = (byte) (base - negated.digits[i] - carry);
            carry = 1;
        }
        for (int i = 0 ; i < negated.repetend.length; i++) {
            negatedRepetend[i] = (byte) (base - negated.repetend[i] - carry);
            carry = 1;
        }
        return AdicDigits.create(negatedRepetend, negatedDigits);

    }

    static byte[] ensureCapacity(int i, byte[] bytes) {
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
    private static class CarryAndIndex {
        final int carry;
        final int index;

        @Override
        public String toString() {
            return index + " carry " + carry;
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private static class MultiplierAndNewDigitAndCarry {
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
    private static class CarryAndIndices {
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

    public static String adicToString(int base, AdicDigits digits) {
        return adicToString((byte) base, digits);
    }

    public static String adicToString(byte base, AdicDigits digits) {
        return digits.toString() + TextUtils.subscript(base);
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
