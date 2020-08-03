package org.meeuw.math.abstractalgebra.complex;

import lombok.Getter;
import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.abstractalgebra.NumberField;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumber<F extends NumberFieldElement<F>> implements FieldElement<ComplexNumber<F>> {

	private final F real;
	private final F imaginairy;
	@Getter
	private final NumberField<F> elementStructure;


	public static <F extends NumberFieldElement<F>> ComplexNumber<F> of(F r, F imaginairy) {
		return new ComplexNumber<>(r, imaginairy);
	}

	public ComplexNumber(F real, F imaginairy) {
		this.real = real;
		this.imaginairy = imaginairy;
		this.elementStructure = real.structure();
	}

	@Override
	public ComplexNumbers<F> structure() {
		return new ComplexNumbers<F>(elementStructure);
	}

	@Override
	public ComplexNumber<F> self() {
		return this;
	}

	@Override
	public ComplexNumber<F> times(ComplexNumber<F> multiplier) {
		return new ComplexNumber<>(
				this.real.times(multiplier.real).minus(this.imaginairy.times(multiplier.imaginairy)),
				this.real.times(multiplier.imaginairy).plus(this.imaginairy.times(multiplier.real)));
	}

	@Override
	public ComplexNumber<F> pow(int exponent) {
		return null;
	}

	@Override
	public ComplexNumber<F> plus(ComplexNumber<F> summand) {
		return new ComplexNumber<>(
				this.real.plus(summand.real),
				this.imaginairy.plus(summand.imaginairy)
		);
	}

	@Override
	public ComplexNumber<F> negation() {
		return new ComplexNumber<>(
				this.real.negation(),
				this.imaginairy.negation()
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ComplexNumber<?> that = (ComplexNumber<?>) o;

		if (real != null ? !real.equals(that.real) : that.real != null) return false;
		return imaginairy != null ? imaginairy.equals(that.imaginairy) : that.imaginairy == null;
	}

	@Override
	public int hashCode() {
		int result = real != null ? real.hashCode() : 0;
		result = 31 * result + (imaginairy != null ? imaginairy.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return real.toString() + imaginairy.toString() + "i";

	}
}
