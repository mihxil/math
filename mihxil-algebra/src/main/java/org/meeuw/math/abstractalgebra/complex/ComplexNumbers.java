package org.meeuw.math.abstractalgebra.complex;

import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.NumberField;
import org.meeuw.math.abstractalgebra.NumberFieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ComplexNumbers<F extends NumberFieldElement<F>> implements Field<ComplexNumber<F>> {

	private final NumberField<F> field;

	public ComplexNumbers(NumberField<F> field) {
		this.field = field;
	}

	@Override
	public ComplexNumber<F> zero() {
		return new ComplexNumber<>(this.field.zero(), this.field.zero());
	}

	@Override
	public ComplexNumber<F> one() {
		return new ComplexNumber<>(this.field.one(), this.field.zero());
	}

	public ComplexNumber<F> i() {
		return new ComplexNumber<>(this.field.zero(), this.field.one());
	}

}
