package org.meeuw.math.abstractalgebra.complex;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class GaussianRationalTest implements
    FieldTheory<GaussianRational>,
    WithScalarTheory<GaussianRational, RationalNumber> {

    static final GaussianRationals structure = GaussianRationals.INSTANCE;

    @Test
    public void string() {
        assertThat(structure.zero().toString()).isEqualTo("0");
        assertThat(structure.one().toString()).isEqualTo("1");
        assertThat(structure.one().negation().toString()).isEqualTo("-1");
        assertThat(structure.i().toString()).isEqualTo("i");
        assertThat(structure.i().negation().toString()).isEqualTo("-i");
        assertThat(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3))).toString()).isEqualTo("3i");
        assertThat(structure.one().plus(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3)))).toString()).isEqualTo("1 + 3i");
        assertThat(structure.one().minus(structure.i().times(structure.getElementStructure().one().times(RationalNumber.of(3)))).toString()).isEqualTo("1 - 3i");

    }

    @Test
    public void isqr() {
        assertThat(structure.i().sqr()).isEqualTo(structure.one().negation());
    }

    @Test
    public void pow3Example() {
        GaussianRational gaussian = new GaussianRational(RationalNumber.of(-77, 100),
            RationalNumber.of(-75, 64));
        assertThat(gaussian.pow(-3).toString()).isEqualTo("⁴⁵⁵⁶³⁰⁴⁷²⁴⁷⁸⁷²⁰⁰⁰⁰⁰⁰⁄₁₂₇₅₂₅₄₉₄₉₅₇₉₄₇₀₁₇₈₄₉ + ⁷⁹⁷⁰⁶⁶⁴⁹⁶⁰⁰⁰⁰⁰⁰⁰⁰⁰⁰⁄₁₂₇₅₂₅₄₉₄₉₅₇₉₄₇₀₁₇₈₄₉i");
    }

    @Test
    public void stream() {
        assertThat(structure.stream().limit(20)).map(AbstractComplexNumber::toString).containsExactly(
            "0",
            "1",
            "i",
            "-1",
            "1 + i",
            "-i",
            "2",
            "-1 + i",
            "1 - i",
            "2i",
            "-2",
            "2 + i",
            "-1 - i",
            "1 + 2i",
            "-2i",
            "¹⁄₂",
            "-2 + i",
            "2 - i",
            "-1 + 2i",
            "1 - 2i"
        );

    }

    @Override
    public Arbitrary<GaussianRational> elements() {
        return Arbitraries.randomValue(
            (random) ->
                new GaussianRational(
                    RationalNumber.of(random.nextInt(200) - 100, random.nextInt(100) + 1),
                    RationalNumber.of(random.nextInt(200) - 100, random.nextInt(100) + 1))
        )
            .injectDuplicates(0.1)
            .edgeCases(config -> {
                config.add(structure.i());
                config.add(structure.one());
                config.add(structure.zero());
            });
    }

    @Override
    public Arbitrary<RationalNumber> scalars() {
        return Arbitraries.of(
            RationalNumber.of(1),
            RationalNumber.of(0),
            RationalNumber.of(2),
            RationalNumber.of(-1)
        );
    }
}
