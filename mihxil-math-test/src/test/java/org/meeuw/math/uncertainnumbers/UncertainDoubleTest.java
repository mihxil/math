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
package org.meeuw.math.uncertainnumbers;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.exceptions.WeighingExactValuesException;
import org.meeuw.theories.numbers.ScalarTheory;
import org.meeuw.math.text.TextUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
class UncertainDoubleTest implements ScalarTheory<UncertainDoubleTest.A> {


    static class A implements UncertainDouble<A> {

        private final double value;
        private final double uncertainty;

        A(double value, double uncertainty) {
            this.value = value;
            this.uncertainty = uncertainty;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public double doubleUncertainty() {
            return uncertainty;
        }

        @Override
        public A immutableInstanceOfPrimitives(double value, double uncertainty) {
            return new A(value, uncertainty);
        }
        @Override
        public String toString() {
            return value + TextUtils.PLUSMIN + uncertainty;
        }

        @SuppressWarnings("com.haulmont.jpb.EqualsDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            return strictlyEquals(o);
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean strictlyEquals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            A a = (A) o;
            return value == a.value;
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


        assertThatThrownBy(() -> a.pow(Integer.MAX_VALUE))
            .isInstanceOf(ArithmeticException.class);
    }

    @Test
    void weightedExacts() {
        A a = new A(2, 0);
        A b = new A(3, 0);
        assertThatThrownBy(() -> {
            a.weightedAverage(b);
        }).isInstanceOf(WeighingExactValuesException.class);

        assertThat(a.weightedAverage(a)).isEqualTo(a);
    }


    @Test
    void weightedNan() {
        A a = new A(3, 1d);
        A b = new A(2, Double.NaN);

        assertThat(a.weightedAverage(b))
            .isEqualTo(new A(2.5, 1d));

        A c = new A(3, Double.NaN);

        assertThat(b.weightedAverage(c))
            .isEqualTo(new A(2.5,Double.NaN));

    }

    @Property
    public void multiply(@ForAll("elements") A a1, @ForAll("elements") A a2) {
        assertThat(a1.times(a2).value).isEqualTo(a1.value * a2.value);
    }

    @Override
    public Arbitrary<A> elements() {
        return Arbitraries.randomValue(
            random -> new A(random.nextDouble() * 200 - 100, random.nextDouble() * 10)
        )
            .dontShrink()
            .edgeCases(config -> {
            config.add(new A(0, 0));
            config.add(new A(1, 0));
        });
    }



}
