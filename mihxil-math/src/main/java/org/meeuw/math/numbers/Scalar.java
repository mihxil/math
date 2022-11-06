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
package org.meeuw.math.numbers;

import java.math.BigInteger;
import java.util.Comparator;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A scalar is the closest thing to a {@link java.lang.Number} interface.
 * <p>
 * If you have some {@link java.lang.Number} and want it as a {@link Scalar}, you could use {@link #ofNumber(java.lang.Number)}. If you have a {@link Scalar} and want it to be a {@link Number}, then  {@link #asNumber()} can be used.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <SELF> self reference
 */
public interface Scalar<SELF extends Scalar<SELF>>
    extends SizeableScalar<SELF, SELF> {

    /**
     * @param number Some {@link java.lang.Number}
     * @return a scalar implementation based on the given number
     * @since 0.9
     */
    static Scalar.Number ofNumber(java.lang.Number number) {
        return new Number(number);
    }

    /**
     * The current scalar can be trivially converted to a {@link java.lang.Number}
     * @since 0.9
     */
    default java.lang.Number asNumber() {
        return new java.lang.Number() {
            @Override
            public int intValue() {
                return Scalar.this.intValue();
            }

            @Override
            public long longValue() {
                return Scalar.this.longValue();
            }

            @Override
            public float floatValue() {
                return Scalar.this.floatValue();
            }

            @Override
            public double doubleValue() {
                return Scalar.this.doubleValue();
            }
        };
    }
    /**
     * A {@link Scalar} is very similar to a {@link Number}, but it <em>is</em> not one. A number is not an interface,
     * so that would be hard. The other way around it simple though, which is what this class is.
     */
    class Number extends java.lang.Number implements  Scalar<Number> {
        private final java.lang.Number number;

        public Number(java.lang.Number number) {
            this.number = number;
        }

        @Override
        public int compareTo(@NonNull Number o) {
            return Comparator.comparingDouble(java.lang.Number::doubleValue).compare(this.number, o.number);
        }

        @Override
        public int intValue() {
            return number.intValue();
        }

        @Override
        public long longValue() {
            return number.longValue();
        }

        @Override
        public BigInteger bigIntegerValue() {
            return BigInteger.valueOf(number.longValue());
        }

        @Override
        public float floatValue() {
            return number.floatValue();
        }

        @Override
        public double doubleValue() {
            return number.doubleValue();
        }

        @Override
        public int signum() {
            if (number.doubleValue() == 0d) {
                return 0;
            }
            return number.doubleValue() < 0 ? -1 : 1;
        }

        @Override
        public Number abs() {
            return isNegative() ? new Number(doubleValue() * -1) : this;
        }
    }

}
