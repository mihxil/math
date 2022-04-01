package org.meeuw.math.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

/**
 * A scalar is the closest thing to a {@link java.lang.Number} interface
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <SELF> self reference
 */
public interface Scalar<SELF extends Scalar<SELF>>
    extends SizeableScalar<SELF, SELF> {

    class Number extends java.lang.Number implements  Scalar<Number> {
        private final java.lang.Number number;

        public Number(java.lang.Number number) {
            this.number = number;
        }

        @Override
        public int compareTo(Number o) {
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
        public BigDecimal bigDecimalValue() {
            return BigDecimal.valueOf(doubleValue());
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
