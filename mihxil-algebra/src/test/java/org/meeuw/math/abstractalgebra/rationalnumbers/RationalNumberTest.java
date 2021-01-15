package org.meeuw.math.abstractalgebra.rationalnumbers;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */

@Log4j2
class RationalNumberTest implements FieldTheory<RationalNumber>, SignedNumberTheory<RationalNumber> {

    @Test
    public void test() {
        assertThat(of(1, 4).times(of(1, 2))).isEqualTo(of(1, 8));

        assertThat(of(2, 5).times(of(1, 2))).isEqualTo(of(1, 5));

        assertThat(of(3, 7).reciprocal()).isEqualTo(of(7, 3));
        assertThat(of(3, 7).reciprocal().times(of(3, 7))).isEqualTo(INSTANCE.one());

        assertThat(of(3, 7).plus(INSTANCE.zero())).isEqualTo(of(6, 14));

        assertThat(of(10).dividedBy(of(3))).isEqualTo(of(10, 3));
        assertThat(of(1).dividedBy(of(3))).isEqualTo(of(1, 3));

        assertThatThrownBy(() -> of(10, 0)).isInstanceOf(InvalidElementCreationException.class);
    }



    @Test
    public void stream() {
        assertThat(INSTANCE
            .stream()
            .limit(30)
            .map(RationalNumber::toString))
            .containsExactly(
                "0",
                "1",
                "-1",
                "2",
                "-2",
                "¹⁄₂",
                "-¹⁄₂",
                "3",
                "-3",
                "¹⁄₃",
                "-¹⁄₃",
                "4",
                "-4",
                "³⁄₂",
                "-³⁄₂",
                "²⁄₃",
                "-²⁄₃",
                "¹⁄₄",
                "-¹⁄₄",
                "5",
                "-5",
                "¹⁄₅",
                "-¹⁄₅",
                "6",
                "-6",
                "⁵⁄₂",
                "-⁵⁄₂",
                "⁴⁄₃",
                "-⁴⁄₃",
                "³⁄₄"
            );
    }
    @Test
    public void reverseStream() {
        assertThat(INSTANCE.reverseStream(10)).map(RationalNumber::toString).containsExactly(
            "¹⁄₃", "-3", "3", "-¹⁄₂", "¹⁄₂", "-2", "2", "-1", "1", "0"
        );


    }

    @Override
	@Provide
    public Arbitrary<RationalNumber> elements() {
        return Arbitraries.randomValue(INSTANCE::nextRandom);
    }


    @Test
    public void all() {
        INSTANCE.stream().limit(100).forEach(i -> {
            log.info(i.toString() + ":" + i.bigDecimalValue());
        });
    }


}
