package org.meeuw.math.abstractalgebra.padic;

import lombok.EqualsAndHashCode;

import java.math.BigInteger;

import org.meeuw.math.ArrayUtils;
import org.meeuw.math.DigitUtils;
import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigits;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigitUtils;
import org.meeuw.math.exceptions.*;

import static org.meeuw.math.abstractalgebra.padic.impl.AdicDigits.NOT_REPETITIVE;
import static org.meeuw.math.abstractalgebra.padic.impl.AdicDigitUtils.*;

/**
 * WIP
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
    public PAdicIntegers getStructure() {
        return structure;
    }

    @Override
    public PAdicInteger dividedBy(long divisor) {
        return null;
    }

    @Override
    public PAdicInteger times(long multiplier) {
        AdicDigits mult = AdicDigits.create(DigitUtils.toBase(structure.base, multiplier), new byte[0]);
        return new PAdicInteger(structure, multiplyAdicDigits((byte) structure.base, this.digits, mult));
    }

    @Override
    public PAdicInteger reciprocal() throws ReciprocalException {
        return null;
    }

    @Override
    public PAdicInteger times(PAdicInteger multiplier) {
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
