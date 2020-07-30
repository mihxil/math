package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.AdditiveMonoidElement;
import org.meeuw.math.abstractalgebra.AlgebraicNumber;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;

import javax.validation.constraints.Min;

/**
 *  The natural numbers ℕ
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NaturalNumber extends AlgebraicNumber<NaturalNumber> implements MultiplicativeMonoidElement<NaturalNumber>, AdditiveMonoidElement<NaturalNumber>
 {
	public static NaturalNumber ZERO = of(0);
	public static NaturalNumber ONE = of(1);

	private final @Min(0) long value;

	public static NaturalNumber of(@Min(0) long value) {
		return new NaturalNumber(value);
	}

	public NaturalNumber(@Min(0) long value) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	@Override
	public NaturalNumber plus(NaturalNumber summand) {
		return of(value + summand.value);
	}

	@Override
	public NaturalNumbers structure() {
		return NaturalNumbers.INSTANCE;
	}

	@Override
	public NaturalNumber self() {
		return this;
	}

	@Override
	public NaturalNumber times(NaturalNumber summand) {
		return of(value * summand.value);
	}

	 @Override
	 public int compareTo(Number o) {
		 return Long.compare(value, o.longValue());
	 }

	 @Override
	 public long longValue() {
		 return value;
	 }

	 @Override
	 public double doubleValue() {
		 return value;
	 }

	 @Override
	 public String toString() {
		return String.valueOf(value);
	 }

	 @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 NaturalNumber that = (NaturalNumber) o;

		 return value == that.value;
	 }

	 @Override
	 public int hashCode() {
		 return (int) (value ^ (value >>> 32));
	 }
 }
