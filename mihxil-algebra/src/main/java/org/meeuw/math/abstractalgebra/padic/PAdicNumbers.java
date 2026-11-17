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
import org.meeuw.math.abstractalgebra.padic.text.PAdicConfiguration;
import org.meeuw.math.abstractalgebra.padic.text.PAdicNumberFormat;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.exceptions.NotParsable;
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

    /**
     * Parses a p-adic number. Accepted are
     * <ul>
     *   <li>p-adic digits in {@link PAdicConfiguration.Notation#ELLIPSIS ellipsis notation}: {@code ...} followed by the repetend, a space, and the other
     *   digits (which may contain a point), optionally followed by the base as a subscript. E.g. {@code ...4 3.21₅}</li>
     *   <li>p-adic digits in {@link PAdicConfiguration.Notation#OVERLINE overline notation}: the repetend overlined, followed by the other digits,
     *   and optionally the base as a subscript. E.g. {@code 4̅3.21₅}</li>
     *   <li>p-adic digits without a repetend (so a repetend of zeros), followed by the base as a subscript (which is required then), e.g. {@code 12₅}</li>
     *   <li>anything else is parsed as a {@link RationalNumbers#fromString(String) rational number}, e.g. {@code 12}, {@code 1/3}, {@code 0.5} (in base 10)</li>
     * </ul>
     * Digits above 9 are letters ({@code a} is 10, {@code b} is 11, etc.). All forms may be preceded by a minus sign.
     */
    @Override
    public PAdicNumber fromString(String string) throws NotParsable {
        String s = string.strip();
        if (s.startsWith("-")) {
            return fromString(s.substring(1)).negation();
        }
        if (s.startsWith(PAdicNumberFormat.ELLIPSIS)) {
            throw new NotParsable("Not all digits of the repetend are given", string);
        }
        final String baseSuffix = TextUtils.subscript(base);
        final boolean hasBaseSuffix = s.endsWith(baseSuffix);
        if (hasBaseSuffix) {
            s = s.substring(0, s.length() - baseSuffix.length()).strip();
        }
        if (!s.isEmpty() && !TextUtils.unsubscript(s.substring(s.length() - 1)).equals(s.substring(s.length() - 1))) {
            throw new NotParsable("Not a " + base + "-adic number", string);
        }
        final String repetend;
        final String digits;
        if (s.startsWith(REPETEND_PREFIX)) {
            String[] parts = s.substring(REPETEND_PREFIX.length()).strip().split("\\s+", 2);
            repetend = parts[0];
            digits = parts.length > 1 ? parts[1] : "";
        } else if (s.indexOf(OVERLINE) > 0) {
            // the overlined characters at the start are the repetend
            StringBuilder overlined = new StringBuilder();
            int i = 0;
            while (i + 1 < s.length() && s.charAt(i + 1) == OVERLINE) {
                overlined.append(s.charAt(i));
                i += 2;
            }
            repetend = overlined.toString();
            digits = s.substring(i);
        } else if (hasBaseSuffix) {
            repetend = "0";
            digits = s;
        } else {
            try {
                return of(RationalNumbers.INSTANCE.fromString(s));
            } catch (RuntimeException e) {
                throw new NotParsable(e.getMessage(), e, string);
            }
        }
        // digits above 9 are letters (a = 10, b = 11, ...)
        if (!repetend.toLowerCase().matches("[0-9a-z]+") || !digits.toLowerCase().matches("[0-9a-z]*(\\.[0-9a-z]*)?")) {
            throw new NotParsable("Expected p-adic digits, like ...4 3.21" + baseSuffix + " or " + TextUtils.overLine("4") + "3.21" + baseSuffix, string);
        }
        try {
            return of(repetend.toLowerCase(), digits.toLowerCase());
        } catch (RuntimeException e) {
            throw new NotParsable(e.getMessage(), e, string);
        }
    }

    private static final String REPETEND_PREFIX = "...";
    private static final char OVERLINE = '\u0305';

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

    @Override
    public Optional<String> getDescription() {
        return Optional.of("The %d-adic numbers: numbers with infinitely many digits (in base %d) before the point, and finitely many after it. Supported are those of which the digits eventually repeat, which are exactly the rational numbers. They are close to each other if their difference is divisible by a high power of %d.".formatted(base, base, base));
    }
}
