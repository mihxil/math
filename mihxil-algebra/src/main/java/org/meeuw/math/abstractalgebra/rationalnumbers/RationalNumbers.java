/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.rationalnumbers;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.GaussianRationals;
import org.meeuw.math.abstractalgebra.reals.BigDecimalField;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.operators.*;
import org.meeuw.math.streams.StreamUtils;

import static java.math.BigInteger.ONE;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.ADDITION;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.MULTIPLICATION;

/**
 * Implementation for the field of Rational Numbers, commonly referred to as ℚ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @see org.meeuw.math.abstractalgebra.rationalnumbers
 */
@Example(ScalarField.class)
@Singleton
public class RationalNumbers extends AbstractAlgebraicStructure<RationalNumber>
    implements ScalarField<RationalNumber>, Streamable<RationalNumber>, Randomizable<RationalNumber> {

    public static final RationalNumbers INSTANCE = new RationalNumbers();

    private static final RationalNumber APPROX_PI = RationalNumber.of(new BigInteger(Utils.PI.replace(".", ""), 10), BigInteger.TEN.pow(Utils.PI.length() - 2));

    private RationalNumbers() {
        super(RationalNumber.class);
    }

    @Override
    public RationalNumber zero() {
        return RationalNumber.ZERO;
    }

    @Override
    public RationalNumber one() {
        return RationalNumber.ONE;
    }

    @Override
    public NavigableSet<AlgebraicComparisonOperator> getSupportedComparisonOperators() {
        return BasicComparisonOperator.ALL;
    }

    @Override
    @DubiousOverride("cheerpj")
    public NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return Field.OPERATORS;
    }

    @Override
    @DubiousOverride("cheerpj")
    public boolean isCommutative(AlgebraicBinaryOperator operator) {
        if (operator.equals(MULTIPLICATION)) {
            return multiplicationIsCommutative();
        }
        if (operator.equals(ADDITION)) {
            return additionIsCommutative();
        }
        return AlgebraicStructure.defaultIsCommutative(operator, this);
    }

    @Override
    @NonExact
    public RationalNumber pi() {
        return APPROX_PI;
    }

    @Override
    public Set<AlgebraicStructure<?>> getSuperGroups() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BigDecimalField.INSTANCE,
            RealField.INSTANCE,
            GaussianRationals.INSTANCE
        )));
    }

    @Override
    public Stream<RationalNumber> stream() {
        return Stream.concat(
            Stream.of(zero()),
            StreamUtils.diagonalStream(
                (s) -> StreamUtils.reverseBigIntegerStream(BigInteger.valueOf(s), false),
                () -> StreamUtils.bigIntegerStream(ONE, false),
                (a, b) -> a.abs().gcd(b).equals(ONE) ? new RationalNumber(a.negate(), b) : null)
                .filter(Objects::nonNull)
                .flatMap(s -> Stream.of(s, s.negation()))
        );
    }

    @Override
    public RationalNumber nextRandom(Random random) {
        long numerator = random.nextLong();
        long denumator = 0L;
        while (denumator == 0L) {
            denumator = random.nextLong();
        }
        return RationalNumber.of(numerator, denumator);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RationalNumbers;
    }

    @Override
    public String toString() {
        return "ℚ";
    }

    static Pattern PATTERN = Pattern.compile("^\\s*(?:(-?\\d+)\\s+)?(-?\\d+)\\s*/\\s*(-?\\d+)|(-?\\d+)\\s*$");
    @Override
    public RationalNumber parse(String s) {
        Matcher m = PATTERN.matcher(s);
        if (!m.matches()) {
            throw new NotParsable("Invalid rational number: '" + s + "'");
        }
        if (m.group(4) != null) {
            // Just an integer
            return RationalNumber.of(new BigInteger(m.group(4)), BigInteger.ONE);
        } else if (m.group(1) != null && m.group(2) != null && m.group(3) != null) {
            // Mixed number: whole numerator/denominator
            BigInteger w = new BigInteger(m.group(1));
            BigInteger n = new BigInteger(m.group(2));
            BigInteger d = new BigInteger(m.group(3));
            BigInteger num = w.abs().multiply(d).add(n);
            if (w.signum() < 0) num = num.negate();
            return RationalNumber.of(num, d);
        } else if (m.group(2) != null && m.group(3) != null) {
            // Just a fraction
            return RationalNumber.of(new BigInteger(m.group(2)), new BigInteger(m.group(3)));
        } else {
            throw new NotParsable("Invalid rational number: " + s);
        }
    }
}
