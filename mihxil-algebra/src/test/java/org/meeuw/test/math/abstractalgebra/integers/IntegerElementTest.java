package org.meeuw.test.math.abstractalgebra.integers;

import java.util.stream.Collectors;

import net.jqwik.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.test.*;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.test.SizeableScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.IntegerElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class IntegerElementTest implements
    RingTheory<IntegerElement>,
    MultiplicativeMonoidTheory<IntegerElement>,
    SizeableScalarTheory<IntegerElement, IntegerElement>,
    SignedNumberTheory<IntegerElement> {

    @BeforeAll
    public static void setup() {
        PositiveIntegerTest.setup();
    }

    @Override
    @Property
    public void one(
        @ForAll(ELEMENTS) IntegerElement v) {
        assertThat(v.times(v.getStructure().one())).isEqualTo(v);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void test() {
        assertThat(of(0).plus(of(1))).isEqualTo(of(1));
        assertThat(of(1).repeatedPlus(8)).isEqualTo(of(8));
        assertThat(of(1).minus(of(5).negation())).isEqualTo(of(6));

        assertThat(of(2).times(of(-5))).isEqualTo(of(-10));
        IntegerElement two = of(2);
        assertThat(two.times(two.getStructure().one())).isEqualTo(two);

        assertThat(two.plus(two.getStructure().zero())).isEqualTo(two);

        assertThat(two.pow(0)).isEqualTo(of(1));
        assertThatThrownBy(() -> two.pow(-1)).isInstanceOf(ReciprocalException.class);
    }



    @Property
    void eucledianDivision(@ForAll(ELEMENTS) IntegerElement e1, @ForAll(ELEMENTS) IntegerElement e2) {
        Assume.that(!e2.isZero());
        assertThat(e1.dividedBy(e2).getValue()).isEqualTo(e1.getValue().divide(e2.getValue()));
        assertThat(e1.dividedBy(e2).times(e2).plus(e1.mod(e2))).isEqualTo(e1);
    }

    @Property
    void strings(@ForAll(ELEMENTS) IntegerElement integerElement) {
        assertThat(integerElement.toString()).isEqualTo(Long.toString(integerElement.longValue()));
    }

    @Property
    void doubles(@ForAll(ELEMENTS) IntegerElement integerElement) {
        assertThat(integerElement.doubleValue()).isEqualTo(Double.valueOf(integerElement.longValue()));
    }


    @Test
    void stream() {
        assertThat(Integers.INSTANCE.stream().limit(11).map(IntegerElement::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 1L, -1L, 2L, -2L, 3L, -3L, 4L, -4L, 5L, -5L);
    }

    @Test
    void determinant2() {
        IntegerElement determinant2 = Integers.INSTANCE
            .determinant(
                new IntegerElement[][]{
                    new IntegerElement[]{of(1), of(2)},
                    new IntegerElement[]{of(3), of(4)}
                }
            );
        assertThat(determinant2).isEqualTo(of(-2));
    }

    @Test
    void determinant3() {
        IntegerElement determinant2 = Integers.INSTANCE
            .determinant(
                new IntegerElement[][]{
                    new IntegerElement[]{of(1),  of(2), of(6)},
                    new IntegerElement[]{of(3),  of(4), of(7)},
                    new IntegerElement[]{of(-1), of(2), of(8)},
                }
            );
        assertThat(determinant2).isEqualTo(of(16));
    }

    @Override
    @Provide
    public Arbitrary<IntegerElement> elements() {
        return Arbitraries.frequencyOf(
            Tuple.of(1, Arbitraries.of(IntegerElement.ZERO, IntegerElement.ONE, IntegerElement.ONE.negation())),
            Tuple.of(20, Arbitraries.randomValue((random) -> IntegerElement.of(random.nextInt())))
        ).injectDuplicates(0.1)
            .edgeCases(integerElementConfig ->
                integerElementConfig.add(IntegerElement.of(2 * 2 * 2 * 2 * 2))
            );

    }




}
