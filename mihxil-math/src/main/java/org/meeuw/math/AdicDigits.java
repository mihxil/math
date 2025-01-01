package org.meeuw.math;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

import static java.lang.System.arraycopy;
import static org.meeuw.math.ArrayUtils.*;

/**
 * Helper class for n-adic digits. So, an infinite number of digits (only supported with repetition) followed by a finite number of digits.
 * The result may be finite, e.g. ...0 1234 is finite.
 * And, p-adically, also things like ...9 are finite (in this case -1).
 * <p>
 * The base of adicity is not in this class, and should be added as extra context if relevant (as a parameter or so).
 * <p>
 */
@EqualsAndHashCode
public class AdicDigits {

    public static final byte[] NOT_REPETITIVE = new byte[]{0};
    public static final byte[] ONLY_REPETITIVE = new byte[]{};


    /**
     * The repetitive part of the digits
     * As a byte array, with the least significant digit at {@code [0]}
     */
    public final byte[] repetend;

    /**
     * The non-repetitive part of the digits
     * As a byte array, with the least significant digit at {@code [0]}
     */
    public final byte[] digits;

    /**
     * @see #digits
     * @see #repetend
     */
    AdicDigits(byte[] repetitive, byte[] digits) {
        this.repetend = repetitive;
        this.digits = digits;
        assert this.repetend.length > 0;
    }

    /**
     * Normalize and create
     *
     * @param repetitive The repetitive digits (in internal (inversed) format)
     * @param digits     The normal digits (in internal (inversed) format)
     */
    public static AdicDigits create(byte[] repetitive, byte[] digits) {
        if (ArrayUtils.allEqualTo(repetitive, (byte) 0)) {
            repetitive = NOT_REPETITIVE;
            digits = removeTrailingZeros(digits);
        }
        // normalize, if
        while (digits.length > 0 && digits[digits.length - 1] == repetitive[repetitive.length - 1]) {
            repetitive = rotate(repetitive, 1);
            byte[] newDigits = new byte[digits.length - 1];
            arraycopy(digits, 0, newDigits, 0, newDigits.length);
            digits = newDigits;
        }
        return new AdicDigits(repetitive, digits);
    }


    public static AdicDigits of(String repetend, String digits) {
        return create(DigitUtils.stringToInverseDigits(repetend), DigitUtils.stringToInverseDigits(digits));
    }

    public static AdicDigits of(int... digits) {
        return create(NOT_REPETITIVE, toInverseByteArray(digits));
    }

    public static AdicDigits ofRepetitive(int... digits) {
        return create(toInverseByteArray(digits), ONLY_REPETITIVE);
    }

    public AdicDigits withDigits(int... digits) {
        if (this.digits.length > 0) {
            throw new IllegalArgumentException();
        }
        return create(repetend, toInverseByteArray(digits));
    }

    /**
     * @param repetend The repetitive digits. The first one is the most significant
     */
    public AdicDigits withRepetend(int... repetend) {
        if (this.repetend != NOT_REPETITIVE) {
            throw new IllegalArgumentException();
        }
        return create(toInverseByteArray(repetend), digits);
    }

    public AdicDigits leftShift(int j) {
        if (j == 0) {
            return this;
        }
        if (j < 0) {
            return rightShift(-1 * j);
        }
        byte[] newDigits = new byte[digits.length + j];
        arraycopy(digits, 0, newDigits, j, digits.length);
        return new AdicDigits(repetend, newDigits);
    }

    public AdicDigits rightShift(int j) {
        if (j == 0) {
            return this;
        }
        if (j < 0) {
            return leftShift(-1 * j);
        }
        if (j <= digits.length) {
            byte[] newDigits = new byte[digits.length - j];
            arraycopy(digits, j, newDigits, 0, newDigits.length);
            return new AdicDigits(repetend, newDigits);
        } else {
            j -= digits.length;
            int rotate = j % repetend.length;
            if (rotate != 0) {
                byte[] shifted = new byte[repetend.length];
                for (int i = 0; i < repetend.length; i++) {
                    shifted[i] = repetend[(i + rotate) % repetend.length];
                }
                return new AdicDigits(shifted, ONLY_REPETITIVE);
            } else {
                return new AdicDigits(repetend, ONLY_REPETITIVE);
            }
        }
    }

    public boolean repeating(int i) {
        return i >= digits.length && (repetend != NOT_REPETITIVE);
    }

    public Optional<ByteAndIndex> get(int i) {
        if (repeating(i)) {
            int index = (i - digits.length) % repetend.length;
            return Optional.of(new ByteAndIndex(repetend[index], digits.length + index, true));
        } else {
            if (i < digits.length) {
                return Optional.of(new ByteAndIndex(digits[i], i, false));
            } else {
                return Optional.empty();
            }
        }
    }

    public int getIndex(int i) {
        if (repeating(i)) {
            return digits.length + (i - digits.length) % repetend.length;
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
        for (int i = repetend.length - 1; i >= 0; i--) {
            builder.append(DigitUtils.digitToString(repetend[i]));
        }
        builder.append(' ');
        for (int i = digits.length - 1; i >= 0; i--) {
            builder.append(DigitUtils.digitToString(digits[i]));
        }
        return builder.toString();
    }

    @ToString
    @EqualsAndHashCode
    public static class ByteAndIndex {
        final byte value;
        final int index;
        final boolean repeating;

        ByteAndIndex(byte value, int index, boolean repeating) {
            this.value = value;
            this.index = index;
            this.repeating = repeating;
        }
    }
}
