package org.meeuw.math;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import org.meeuw.math.text.TextUtils;

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
            result += pow * d ;
            pow *= base;
        }
        return result;

    }


    public static byte[] multiplyInverseDigits(byte base, byte digit, byte[] multiplicand) {
        byte[] result = new byte[multiplicand.length + 1];
        int carry = 0;
        for (int i = 0 ; i < multiplicand.length;i ++) {
            int ri = carry ;
            ri += digit * multiplicand[i];
            result[i] = (byte) (ri % base);
            carry = ri / base;
        }
        result[result.length - 1] = (byte) (carry);
        return result;
    }

     public static AdicDigits multiplyAdicDigits(byte base, byte digit, AdicDigits multiplicand) {
        byte[] resultdigits = multiplyInverseDigits(base, digit, multiplicand.digits);

        int carry = resultdigits[resultdigits.length - 1];
         for (int i = 0 ; i < multiplicand.repetitive.length;i ++) {
            int ri = carry ;
            ri += digit * multiplicand.repetitive[i];
           // result[i] = (byte) (ri % base);
            carry = ri / base;
        }
        return new AdicDigits(new byte[]{0}, resultdigits);
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
                System.arraycopy(result[i], 0, newResult, i, result[i].length);
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
        byte[] result = new byte[max];
        for (int i = 0 ; i < max;i ++) {
            int ri = carry ;
            for (byte[] b : a) {
                ri += digit(i, b);
            }
            result[i] = (byte) (ri % base);
            carry = ri / base;
        }
        while (carry != 0) {
            byte[] newResult = new byte[result.length + 1];
            System.arraycopy(result, 0, newResult, 0, result.length);
            result = newResult;
            result[result.length - 1] = (byte) (carry % base);
            carry /= base;
        }
        return result;
    }


    /**
     * Performs a sum of number of integers
     * @param base  The base of this number
     */
    public static AdicDigits sumAdicDigits(byte base, AdicDigits... ad) {
        byte[] result = new byte[100];
        int carry = 0;
        int i = 0;
        List<CarryAndSum> detecting = new ArrayList<>();
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
                CarryAndSum check = new CarryAndSum(carry, r);
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
        System.arraycopy(result, 0, digits, 0, digits.length);
        System.arraycopy(result, i - detecting.size(), repetitive, 0, repetitive.length);
        return new AdicDigits(repetitive, digits);

    }

    static byte[] ensureCapacity(int i, byte[] bytes) {
        if (bytes.length > i) {
            return bytes;
        } else {
            byte[] newBytes = new byte[bytes.length + 100];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
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
        final byte[] repetitive;
        final byte[] digits;

        public AdicDigits(byte[] repetitive, byte[] digits) {
            this.repetitive = repetitive;
            this.digits = digits;
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

    }

    @AllArgsConstructor
    @EqualsAndHashCode
    public static class CarryAndSum {
        final int carry;
        final int sum;
        @Override
        public String toString() {
            return carry + "," + sum;
        }
    }

    public static String adicToString(byte base, AdicDigits digits) {
        StringBuilder builder = new StringBuilder();
        builder.append("...");
        for (int i = digits.repetitive.length - 1 ; i >= 0; i--) {
            builder.append(digitToString(digits.repetitive[i]));
        }
        builder.append(' ');
        for (int i = digits.digits.length -1 ; i >= 0; i--) {
            builder.append(digitToString(digits.digits[i]));
        }
        builder.append(TextUtils.subscript(base));
        return builder.toString();
    }

    private static String digitToString(int i) {
        if (i < 10) {
            return Integer.toString(i);
        }
        if (i < 36) {
            return Character.toString('a' + (i - 10));
        }
        throw new UnsupportedOperationException();
    }



}
