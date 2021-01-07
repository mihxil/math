package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
strictfp class UncertainDoubleTest {

    @Getter
    static class A implements UncertainDouble<A> {

        private final double value;
        private final double uncertainty;

        A(double value, double uncertainty) {
            this.value = value;
            this.uncertainty = uncertainty;
        }

        @Override
        public A of(double value, double uncertainty) {
            return new A(value, uncertainty);
        }
    }

    @Test
    void dividedBy() {
         A a = new A(1, 0.1).dividedBy(2);
         assertThat(a.value).isEqualTo(0.5);
         assertThat(a.uncertainty).isEqualTo(0.05);
    }

    @Test
    void plusDouble() {
         A a = new A(1, 0.1).plus(2);
         assertThat(a.value).isEqualTo(3);
         assertThat(a.uncertainty).isEqualTo(0.1);
    }

    @Test
    void minus() {
         A a = new A(1, 0.1).minus(2);
         assertThat(a.value).isEqualTo(-1);
         assertThat(a.uncertainty).isEqualTo(0.1);
    }

    @Test
    void negation() {
         A a = new A(1, 0.1).negation();
         assertThat(a.value).isEqualTo(-1);
         assertThat(a.uncertainty).isEqualTo(0.1);
    }


    @Test
    void times() {
        A a = new A(2, 0.1);
        A b = new A(3, 0.2);
        A product = a.times(b);
        assertThat(product.value).isEqualTo(6);
        assertThat(product.uncertainty).isEqualTo(0.4714420993730031);
    }

    @Test
    void plus() {
        A a = new A(2, 0.1);
        A b = new A(3, 0.2);
        A sum = a.plus(b);
        assertThat(sum.value).isEqualTo(5);
        assertThat(sum.uncertainty).isEqualTo(0.223606797749979);
    }

    @Test
    void pow() {
        A a = new A(2, 0.1);
        A power = a.pow(3);
        assertThat(power.value).isEqualTo(8);
        assertThat(power.uncertainty).isEqualTo(1.2000000000000002);
    }



}
