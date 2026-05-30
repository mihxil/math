package org.meeuw.math.abstractalgebra.padic;

import lombok.EqualsAndHashCode;

import java.math.BigInteger;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigits;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigitUtils;
import org.meeuw.math.exceptions.*;

import static org.meeuw.math.abstractalgebra.padic.impl.AdicDigits.NOT_REPETITIVE;
import static org.meeuw.math.abstractalgebra.padic.impl.AdicDigitUtils.*;

/**
 * WIP
 * @see PAdicIntegers
 */
@EqualsAndHashCode
public class PAdicInteger implements FieldElement<PAdicInteger> {


    final AdicDigits digits;
    final PAdicIntegers structure;

    PAdicInteger(PAdicIntegers structure, AdicDigits digits) {
        this.digits = digits;
        this.structure = structure;
        for (byte i : digits.digits) {
            if (Byte.toUnsignedInt(i) >  structure.base)  {
                throw new InvalidElementCreationException("digit must be smaller than " + structure.base);
            }
        }
    }
    PAdicInteger(PAdicIntegers structure, int... digits) {
        this(structure, AdicDigits.of(digits));
    }


    public BigInteger bigIntegerValue() {
        if (! ArrayUtils.equals(digits.repetend , NOT_REPETITIVE)) {
            throw new NotFiniteException(this + " is not finite");
        }
        BigInteger result = BigInteger.ZERO;
        BigInteger pow = BigInteger.ONE;
        for (byte digit : digits.digits) {
            result = result.add(pow.multiply(BigInteger.valueOf(Byte.toUnsignedInt(digit))));
            pow = pow.multiply(structure.bbase);
        }
        return result;
    }


    @Override
    public @NonNull PAdicIntegers getStructure() {
        return structure;
    }

    @Override
    public PAdicInteger dividedBy(BigInteger divisor) {
        // Division by a BigInteger: convert to PAdicInteger and use reciprocal
        AdicDigits divisorDigits = AdicDigits.create(DigitUtils.toBase(structure.base, divisor.longValue()), new byte[0]);
        PAdicInteger divisorPadic = new PAdicInteger(structure, divisorDigits);
        return times(divisorPadic.reciprocal());
    }

    @Override
    public PAdicInteger times(long multiplier) {
        AdicDigits mult = AdicDigits.create(DigitUtils.toBase(structure.base, multiplier), new byte[0]);
        return new PAdicInteger(structure, multiplyAdicDigits((byte) structure.base, this.digits, mult));
    }

    @Override
    public PAdicInteger times(BigInteger multiplier) {
        AdicDigits mult = AdicDigits.create(DigitUtils.toBase(structure.base, multiplier.longValue()), new byte[0]);
        return new PAdicInteger(structure, multiplyAdicDigits((byte) structure.base, this.digits, mult));
    }

    @Override
    public PAdicInteger reciprocal() throws ReciprocalException {
        // Get the first (least significant) digit
        byte firstDigit = digits.get(0).value();

        // For a p-adic integer to have a reciprocal, its first digit must be non-zero
        // (i.e., it must be a p-adic unit)
        if (firstDigit == 0) {
            throw new ReciprocalException("Cannot compute reciprocal: first digit is 0 (not a p-adic unit)", "1/" + this);
        }

        // Find modular inverse of the first digit mod p
        int modInverse = modularInverse(firstDigit, structure.base);
        if (modInverse == -1) {
            throw new ReciprocalException("Cannot compute modular inverse of " + firstDigit + " mod " + structure.base, "1/" + this);
        }

        // Use Newton iteration (Hensel lifting) to compute reciprocal
        // Start with initial approximation x_0 = modInverse
        // Then iterate: x_{n+1} = x_n * (2 - a * x_n)
        // This doubles precision each iteration

        PAdicInteger two = new PAdicInteger(structure, AdicDigits.of(2));
        PAdicInteger x = new PAdicInteger(structure, AdicDigits.of(modInverse));

        // We need enough iterations to get a repeating pattern
        // Typically this converges quickly for p-adic numbers
        // We'll iterate until we detect a stable pattern or reach max iterations
        int maxIterations = 100;
        PAdicInteger prevX = null;

        for (int i = 0; i < maxIterations; i++) {
            // x = x * (2 - this * x)
            PAdicInteger ax = this.times(x);
            PAdicInteger twoMinusAx = two.minus(ax);
            PAdicInteger newX = x.times(twoMinusAx);

            // Check if we've converged (detected the repeating pattern)
            if (newX.equals(prevX) || newX.equals(x)) {
                return newX;
            }

            prevX = x;
            x = newX;
        }

        // If we reach here, return the best approximation we have
        return x;
    }

    /**
     * Computes the modular inverse of a mod m using extended Euclidean algorithm.
     * Returns -1 if no inverse exists (when gcd(a, m) != 1).
     */
    private static int modularInverse(int a, int m) {
        a = ((a % m) + m) % m; // Ensure positive
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // No inverse exists
    }

    @Override
    public PAdicInteger times(@NonNull PAdicInteger multiplier) {
         return new PAdicInteger(structure, multiplyAdicDigits(structure.base, this.digits, multiplier.digits));
    }



    @Override
    public PAdicInteger plus(PAdicInteger summand) {
        return new PAdicInteger(structure, sumAdicDigits(structure.base,
            this.digits,
            summand.digits
        ));
    }

    byte digit(int n, byte[] digits, byte[] repetitive) {
        return digits.length > n ? digits[n] : repetitive[(n - digits.length) % repetitive.length];
    }

    @Override
    public PAdicInteger negation() {
        return new PAdicInteger(structure, AdicDigitUtils.negate((byte) structure.base, digits));
    }

    public PAdicInteger leftShift(int i) {
        return new PAdicInteger(structure, digits.leftShift(i));
    }

    public PAdicInteger rightShift(int i) {
        return new PAdicInteger(structure, digits.leftShift(i));
    }

    public PAdicInteger withRepetend(int... repetitive) {
        return new PAdicInteger(structure, digits.withRepetend(repetitive));
    }

    @Override
    public String toString() {
        return adicToString(structure.base, digits);
    }


}
