package org.meeuw.math.abstractalgebra.padic;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.DigitUtils;
import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidStructureCreationException;
import org.meeuw.math.text.TextUtils;

public class PAdicIntegers implements Field<PAdicInteger> {
    final byte base;
    final BigInteger bbase;

    private static final Map<Byte, PAdicIntegers> CACHE = new ConcurrentHashMap<>();

    private PAdicIntegers(byte base) {
        this.base = base;
        this.bbase = BigInteger.valueOf(base);
        if (!IntegerUtils.isPrime(this.base)) {
            throw new InvalidStructureCreationException("The base of a p-adic number must be a prime");
        }
    }

    public static PAdicIntegers of(int base) {
        return of((byte) base);
    }

    public static PAdicIntegers of(byte base) {
        return CACHE.computeIfAbsent(base, PAdicIntegers::new);
    }

    public PAdicInteger of(String repitative, String digits) {
        return new PAdicInteger(this, DigitUtils.AdicDigits.of(repitative, digits));
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
