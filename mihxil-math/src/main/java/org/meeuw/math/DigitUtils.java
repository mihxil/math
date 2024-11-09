package org.meeuw.math;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.*;

import org.meeuw.math.text.TextUtils;
import org.meeuw.math.validation.Prime;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.System.arraycopy;
import static org.meeuw.math.ArrayUtils.toInverseByteArray;

/**
 * Utilities related to dealing with integer that are represented an array of digits.
 * A digit is normally represented by a {@code byte}
 * <p>
 * This is targeted e.g. at an implemention of p-adic numbers.
 *
 * @see java.math.BigInteger
 * @since 0.14
 */
public class DigitUtils {

    private DigitUtils() {}


    public static byte[] toBase(byte base, long value) {
        byte[] result = new byte[100];
        int i = 0;
        int basei = toUnsignedInt(base);
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
     * @param digits where the least significant digit is at {@code digits[digits.length - 1]}
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


    public static byte[] multiplyInverseDigits(byte base, byte digit, byte[] multiplicand) {
        byte[] result = new byte[multiplicand.length + 1];
        int basei = toUnsignedInt(base);
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

    public static AdicDigits multiplyAdicDigits(byte base, byte digit, AdicDigits multiplicand) {
        byte[] resultdigits = multiplyInverseDigits(base, digit, multiplicand.digits);
        int basei = toUnsignedInt(base);
        int carry = toUnsignedInt(resultdigits[resultdigits.length - 1]);
        int digiti = toUnsignedInt(digit);
        List<CarryAndIndex> carries = new ArrayList<>();
        List<Byte> moreDigits = new ArrayList<>();
        int i = 0;
        while(true) {
            int index = i % multiplicand.repetitive.length;
            CarryAndIndex carryAndIndex = new CarryAndIndex(carry, index);
            int indexOf = carries.indexOf(carryAndIndex);
            if (indexOf == -1) {
                carries.add(carryAndIndex);
                int ri = carry +  digiti * toUnsignedInt(multiplicand.repetitive[index]);
                moreDigits.add((byte) (ri % basei));
                carry = ri / basei;
                i++;
            } else {
                break;
            }
        }
        byte[] repetitive = new byte[carries.size()];
        for (int j = 0; j < carries.size(); j++) {
            repetitive[j] = moreDigits.remove(0);
        }
        byte[] digits = new byte[moreDigits.size()  + resultdigits.length - 1];
        int j = 0;
        while(!moreDigits.isEmpty()) {
            digits[j++] = moreDigits.remove(0);
        }
        arraycopy(resultdigits, 0, digits, j, resultdigits.length - 1);
        return new AdicDigits(repetitive, digits);
     }


    public static AdicDigits multiplyPAdicDigits(@Prime byte base, AdicDigits multiplicator, AdicDigits multiplicand) {
        //assert IntegerUtils.isPrime(toUnsignedInt(base));
        int i = 0;
        AdicDigits sum = null;
        while(true) {
            byte d = multiplicator.get(i);
            AdicDigits a = multiplyAdicDigits(base, d, multiplicand.timesBasePower(i));
            if (sum == null) {
                sum = a;
            } else {
                sum = sumAdicDigits(base, sum, a);
            }
            i++;
            if (i > 100) {
                break;
            }
        }
        return sum;
     }



    /**
     * Performs 'long multiplication' on two numbers represented by a {@code byte[]}, where the least significant digit is the one at {@code [0]}
     * @param base  The base of this number
     * @see #multiplyInverseDigits(byte, byte, byte[])
     * @see #sumInverseDigits(byte, byte[]...)
     */
    public static byte[] multiplyInverseDigits(byte base, byte[] multiplicand1, byte[] multiplicand2) {

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
     * @param base  The base of this number
     */
    public static byte[] sumInverseDigits(byte base, byte[]... a) {
        int max = maxLength(a);
        int carry = 0;
        int basei = Byte.toUnsignedInt(base);
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
            result[result.length - 1] = (byte) (carry % base);
            carry /= base;
        }
        return result;
    }


    public static AdicDigits sumAdicDigits(int base, AdicDigits... ad) {
        return sumAdicDigits((byte) base, ad);
    }

    /**
     * Performs a sum of number of integers
     * @param base  The base of this number
     */
    public static AdicDigits sumAdicDigits(byte base, AdicDigits... ad) {
        byte[] result = new byte[100];
        int carry = 0;
        int i = 0;
        List<CarryAndIndices> detecting = new ArrayList<>();
        while(true) {
            int r = carry;
            boolean detectRepetiton = true;
            for (AdicDigits d : ad) {
                detectRepetiton &= d.repeating(i);
                r += d.get(i);
            }
            result = ensureCapacity(i, result);
            result[i] = (byte) (r % base);
            i++;
            if (detectRepetiton) {
                CarryAndIndices check = new CarryAndIndices(
                    carry,
                    AdicDigits.getIndexes(i, ad)
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
        return new AdicDigits(repetitive, digits);

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

    /**
     * n-adic digits. So, an infinite number of digits (only supported with repetition) followed by a finite number of digits.
     * The result may be finite, e.g. ...0 1234 is finite.
     * And, p-adically, also things like ...9 are finite (in this case -1).
     *
     */
    public static class AdicDigits {
        public static final byte[] NOT_REPETITIVE = new byte[] {0};

        public byte[] repetitive;
        public byte[] digits;

        public AdicDigits(byte[] repetitive, byte[] digits) {
            this.repetitive = repetitive;
            this.digits = digits;
        }
        public static AdicDigits of(String repetitive, String digits) {
            return new AdicDigits(stringToDigits(repetitive), stringToDigits(digits));
        }

        public static AdicDigits of(int... digits) {
            return new AdicDigits(new byte[] {0}, toInverseByteArray(digits));
        }

        public static AdicDigits ofRepetitive(int... digits) {
            return new AdicDigits(toInverseByteArray(digits) , new byte[0]);
        }

        public AdicDigits repetitive(int... repetitive) {
            return new AdicDigits(toInverseByteArray(repetitive), digits);
        }
        public AdicDigits digits(int... digits) {
            return new AdicDigits(repetitive ,toInverseByteArray(digits));
        }

        public AdicDigits timesBasePower(int j) {
            if (j == 0) {
                return this;
            }
            byte[] newDigits = new byte[digits.length + j];
            arraycopy(digits, 0, newDigits, j, digits.length);
            return new AdicDigits(repetitive, newDigits);
        }

        public boolean repeating(int i) {
            return i >= digits.length;
        }
        public byte get(int i) {
            if (repeating(i)) {
                return repetitive[(i - digits.length) % repetitive.length];
            } else {
                return digits[i];
            }
        }
        public int getIndex(int i) {
            if (repeating(i)) {
                return digits.length + (i - digits.length) % repetitive.length;
            } else {
                return i;
            }
        }
        public static int[] getIndexes(int j, AdicDigits... ca) {
            int[] result = new int[ca.length];
            for (int i = 0; i < ca.length; i++) {
                result[i] = ca[i].getIndex(j);
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("...");
            for (int i = repetitive.length - 1 ; i >= 0; i--) {
                builder.append(digitToString(repetitive[i]));
            }
            builder.append(' ');
            for (int i = digits.length -1 ; i >= 0; i--) {
                builder.append(digitToString(digits[i]));
            }
            return builder.toString();
        }

    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private static class CarryAndIndex {
        final int carry;
        final int index;
    }


    @AllArgsConstructor
    private static class CarryAndIndices {
        final int carry;
        final int[] indices;
        @Override
        public String toString() {
            return carry + "," + ArrayUtils.toString(indices);
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
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
        StringBuilder builder = new StringBuilder();
        builder.append(digits.toString());
        builder.append(TextUtils.subscript(base));
        return builder.toString();
    }

    public static String digitToString(byte b) {
        int i = toUnsignedInt(b);
        if (i < 10) {
            return Byte.toString(b);
        }
        char codePoint = (char) ('a' + i - 10);
        return String.valueOf(codePoint);

    }

    private static byte[] stringToDigits(String s) {
        byte[] result = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(s.length() - 1 - i);
            if (c >= '0' && c <= '9') {
                result[i] = (byte) (c - '0');
            } else {
                result[i] = (byte) (c - 'a');
            }
        }
        return result;
    }



}
