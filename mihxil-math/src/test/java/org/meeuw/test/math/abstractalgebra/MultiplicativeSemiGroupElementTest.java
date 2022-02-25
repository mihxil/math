package org.meeuw.test.math.abstractalgebra;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class MultiplicativeSemiGroupElementTest {

    static class A implements MultiplicativeSemiGroupElement<A> {

        final int value;

        public A(int value) {
            this.value = value;
        }

        @Override
        public MultiplicativeSemiGroup<A> getStructure() {
            return new Struct();
        }

        @Override
        public A times(A multiplier) {
            return new A(value * multiplier.value);
        }
    }
    static class Struct implements MultiplicativeSemiGroup<A> {

        @Override
        public Cardinality getCardinality() {
            return Cardinality.ALEPH_0;
        }

        @Override
        public Class<A> getElementClass() {
            return A.class;
        }

        @Override
        public A nextRandom(Random random) {
            return new A(random.nextInt(100));
        }
    }

    @Test
    void pow() {
        A a = new A(2);
        assertThatThrownBy(() -> a.pow(-1)).isInstanceOf(DivisionByZeroException.class);
        assertThatThrownBy(() -> a.pow(0)).isInstanceOf(ReciprocalException.class);
        assertThat(a.pow(1).value).isEqualTo(2);
        assertThat(a.pow(2).value).isEqualTo(4);
        assertThat(a.pow(3).value).isEqualTo(8);
        assertThat(a.pow(4).value).isEqualTo(16);
        assertThat(a.pow(5).value).isEqualTo(32);
    }

}
