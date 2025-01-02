package org.meeuw.math.abstractalgebra.padic.impl;

import java.util.*;

import org.meeuw.math.DigitUtils;
import org.meeuw.math.text.TextUtils;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.System.arraycopy;
import static org.meeuw.math.DigitUtils.multiplyInverseDigitsWithCarry;

public class Utils {
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
        List<DigitUtils.MultiplierAndNewDigitAndCarry> list = null; // only relevant when repeating digits.
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
                DigitUtils.MultiplierAndNewDigitAndCarry l = new DigitUtils.MultiplierAndNewDigitAndCarry(multiplierDigit.index, multiplierDigit.value, newDigit.value, carry);
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
        List<DigitUtils.CarryAndIndices> detecting = new ArrayList<>();
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
            result = DigitUtils.ensureCapacity(i, result);
            result[i] = (byte) (r % base);
            i++;
            if (detectRepetition) {
                DigitUtils.CarryAndIndices check = new DigitUtils.CarryAndIndices(
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
        List<DigitUtils.CarryAndIndex> carries = new ArrayList<>();
        List<Byte> moreDigits = new ArrayList<>();
        int i = 0;
        byte[] repetitive;
        while(true) {
            int index = i % multiplicand.repetend.length;
            DigitUtils.CarryAndIndex carryAndIndex = new DigitUtils.CarryAndIndex(carry, index);
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


    public static String adicToString(int base, AdicDigits digits) {
        return adicToString((byte) base, digits);
    }

    public static String adicToString(byte base, AdicDigits digits) {
        return digits.toString() + TextUtils.subscript(base);
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

}
