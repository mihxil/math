package org.meeuw.math.abstractalgebra.padic;

import java.math.BigInteger;

import java.util.Arrays;

import org.meeuw.math.DigitUtils;
import org.meeuw.math.DigitUtils.AdicDigits;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.*;

import static org.meeuw.math.DigitUtils.AdicDigits.NOT_REPETITIVE;
/**
* WIP
*/
public class PAdicInteger implements FieldElement<PAdicInteger> {


    final AdicDigits digits;
    final PAdicIntegers structure;

    PAdicInteger(PAdicIntegers structure, AdicDigits digits) {
        this.digits = digits;
        this.structure = structure;
        for (byte i : digits.digits) {
            if (Byte.compareUnsigned(i, structure.base) > 0)  {
                throw new InvalidElementCreationException("digit must be smaller than " + structure.base);
            }
        }
    }
    PAdicInteger(PAdicIntegers structure, byte... digits) {
        this(structure, new AdicDigits(NOT_REPETITIVE, digits));
    }


    public BigInteger bigIntegerValue() {
        if (Arrays.compare(digits.repetitive , NOT_REPETITIVE) != 0) {
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
        AdicDigits mult = new AdicDigits(DigitUtils.toBase(structure.base, multiplier), new byte[0]);
        return new PAdicInteger(structure, DigitUtils.multiplyPAdicDigits(structure.base, this.digits, mult));
    }

    @Override
    public PAdicInteger reciprocal() throws ReciprocalException {
        return null;
    }

    @Override
    public PAdicInteger times(PAdicInteger multiplier) {
         return new PAdicInteger(structure, DigitUtils.multiplyPAdicDigits(structure.base, this.digits, multiplier.digits));
    }

    @Override
    public PAdicInteger plus(PAdicInteger summand) {
        return new PAdicInteger(structure, DigitUtils.sumAdicDigits(structure.base,
            this.digits,
            summand.digits
        ));
    }

    byte digit(int n, byte[] digits, byte[] repetitive) {
        return digits.length > n ? digits[n] : repetitive[(n - digits.length) % repetitive.length];
    }

    @Override
    public PAdicInteger negation() {

        return null;
    }

    @Override
    public String toString() {
        return DigitUtils.adicToString(structure.base, digits);
    }




}
