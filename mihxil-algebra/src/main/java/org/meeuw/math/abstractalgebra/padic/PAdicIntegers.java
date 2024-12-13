package org.meeuw.math.abstractalgebra.padic;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.DigitUtils.AdicDigits;
import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.Cardinality;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.exceptions.InvalidStructureCreationException;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.validation.Prime;

public class PAdicIntegers implements Field<PAdicInteger> {
    final int base;
    // could be byte, but that give a lot of casting, and stuff with toUnsignedInt
    // there are only a few instances of this class, memory usage is no issue.

    final BigInteger bbase;

    private static final Map<Byte, PAdicIntegers> CACHE = new ConcurrentHashMap<>();

    private PAdicIntegers(@Prime int base) {
        this.base = base;
        this.bbase = BigInteger.valueOf(base);
        if (!IntegerUtils.isPrime(this.base)) {
            throw new InvalidStructureCreationException("The base of a p-adic number must be a prime");
        }
    }

    public static PAdicIntegers of(@Prime int base) {
        return of((byte) base);
    }

    public static PAdicIntegers of(@Prime byte base) {
        return CACHE.computeIfAbsent(base, PAdicIntegers::new);
    }

    public PAdicInteger of(String repitend, String digits) {
        return new PAdicInteger(this,
            AdicDigits.of(repitend, digits)
        );
    }
    public PAdicInteger of(byte... digits) {
        return new PAdicInteger(this, digits);
    }

    @Override
    public PAdicInteger zero() {
        return new PAdicInteger(this);
    }

    @Override
    public PAdicInteger one() {
        return new PAdicInteger(this, (byte) 1);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public Class<PAdicInteger> getElementClass() {
        return PAdicInteger.class;
    }

    @Override
    public String toString() {
        return "ℚ" + TextUtils.subscript(base);
    }

}
