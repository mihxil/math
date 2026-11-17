package org.meeuw.math.abstractalgebra.padic;

import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.Randomizable;
import org.meeuw.math.abstractalgebra.Cardinality;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigitUtils;
import org.meeuw.math.abstractalgebra.padic.impl.AdicDigits;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.InvalidStructureCreationException;
import org.meeuw.math.operators.AbstractAlgebraicIntOperator;
import org.meeuw.math.operators.AlgebraicIntOperator;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.validation.Prime;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;
import static org.meeuw.math.CollectionUtils.navigableSet;


/**
 * P-adic numbers are numbers with an infinite amount of digits <em>before</em> the point (and a finite number after it). This implementation
 * supports those numbers of which the digits eventually repeat. Those are written as two groups of digits, both of arbitrary length: one group is
 * 'repeated', and the other group gives the remaining 'least significant' digits (see {@link AdicDigits}).
 * <p>
 * Normal integers are a subset of this, with a {@link AdicDigits#repetend} of just a zero. Negative integers have a repetend of just {@code p - 1}
 * (e.g. {@code ...4 ₅} is {@code -1}).
 * <p>
 * The p-adic numbers with repeating digits are exactly the rational numbers (see {@link AdicDigitUtils}), so that is how {@link PAdicNumber the elements}
 * are implemented. This is a field: every element but zero has a reciprocal (e.g. {@code 1/5 = ...0 0.1₅}, and {@code 1/2 = ...2 3₅}).
 *
 * @see AdicDigits
 */
@EqualsAndHashCode
public class PAdicNumbers implements Field<PAdicNumber>, Randomizable<PAdicNumber> {


    public static final AlgebraicIntOperator LEFT_SHIFT = new AbstractAlgebraicIntOperator("left_shift",  getDeclaredMethod(PAdicNumber.class, "leftShift", int.class), (e, i) -> e + "<<" + i);

    public static final AlgebraicIntOperator RIGHT_SHIFT = new AbstractAlgebraicIntOperator("right_shift",  getDeclaredMethod(PAdicNumber.class, "rightShift", int.class), (e, i) -> e + ">>" + i);


    final int base;
    // could be byte, but that give a lot of casting, and stuff with toUnsignedInt
    // there are only a few instances of this class, memory usage is no issue.

    final BigInteger bbase;

    @EqualsAndHashCode.Exclude
    private final PAdicNumber zero = new PAdicNumber(this, RationalNumber.ZERO);
    @EqualsAndHashCode.Exclude
    private final PAdicNumber one = new PAdicNumber(this, RationalNumber.ONE);

    @Override
    public NavigableSet<AlgebraicIntOperator> getSupportedIntOperators() {
        return navigableSet(INT_OPERATORS, LEFT_SHIFT, RIGHT_SHIFT);
    }

    private static final Map<Byte, PAdicNumbers> CACHE = new ConcurrentHashMap<>();

    private PAdicNumbers(@Prime int base) {
        this.base = base;
        this.bbase = BigInteger.valueOf(base);
        if (!IntegerUtils.isPrime(this.base)) {
            throw new InvalidStructureCreationException("The base of a p-adic number must be a prime");
        }
    }

    public static PAdicNumbers of(@Prime int base) {
        return of((byte) base);
    }

    public static PAdicNumbers of(@Prime byte base) {
        return CACHE.computeIfAbsent(base, PAdicNumbers::new);
    }

    /**
     * @param repetend the digits that are repeated (most significant first)
     * @param digits the other digits (most significant first). It may contain a point, e.g. {@code "12.3"}
     */
    public PAdicNumber of(String repetend, String digits) {
        final int point = digits.indexOf('.');
        final int fractionDigits = point < 0 ? 0 : digits.length() - point - 1;
        final String allDigits = point < 0 ? digits : digits.substring(0, point) + digits.substring(point + 1);
        return of(AdicDigits.of(repetend, allDigits)).rightShift(fractionDigits);
    }

    /**
     * @param digits the digits of a non-negative integer, the most significant first
     */
    public PAdicNumber of(int... digits) {
        return of(AdicDigits.of(digits));
    }

    /**
     * @param digits the p-adic digits (which must be smaller than p)
     */
    public PAdicNumber of(AdicDigits digits) {
        for (byte d : digits.digits) {
            checkDigit(d);
        }
        for (byte d : digits.repetend) {
            checkDigit(d);
        }
        return of(AdicDigitUtils.toRational(base, digits));
    }

    private void checkDigit(byte d) {
        if (Byte.toUnsignedInt(d) >= base) {
            throw new InvalidElementCreationException("digit must be smaller than " + base);
        }
    }

    public PAdicNumber of(RationalNumber value) {
        return new PAdicNumber(this, value);
    }

    public PAdicNumber valueOf(long value) {
        return of(RationalNumber.of(value));
    }

    @Override
    public PAdicNumber zero() {
        return zero;
    }

    @Override
    public PAdicNumber one() {
        return one;
    }

    public int getBase() {
        return base;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public Class<PAdicNumber> getElementClass() {
        return PAdicNumber.class;
    }

    @Override
    public String toString() {
        return "ℚ" + TextUtils.subscript(base);
    }

    @Override
    public PAdicNumber nextRandom(Random r) {

        int[] nonRepetitive = new int[(int) Math.abs(r.nextGaussian() * 10d)];
        for (int i = 0 ; i < nonRepetitive.length; i++) {
            nonRepetitive[i] = r.nextInt(base);
        }
        int[] repetitive = new int[(int) Math.abs(r.nextGaussian() * 10d)];
        for (int i = 0 ; i < repetitive.length; i++) {
            repetitive[i] = r.nextInt(base);
        }
        final PAdicNumber result = of(nonRepetitive)
            .withRepetend(repetitive);
        // sometimes also digits after the point
        final int fractionDigits = r.nextInt(5) == 0 ? 1 + r.nextInt(3) : 0;
        return result.rightShift(fractionDigits);
    }


}
