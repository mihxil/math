package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.util.function.Predicate;

import org.meeuw.math.numbers.NumberOperations;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Getter
public class ConfidenceInterval<N extends Number> implements Predicate<N> {
	private final N low;
	private final N high;
	private final Predicate<N> predicate;

	public static <N extends Number> ConfidenceInterval<N> of(N value, N uncertainty, int interval) {
	    NumberOperations<N> op = NumberOperations.of(value);
		N halfRange = op.multiply(interval, uncertainty);
		return new ConfidenceInterval<>(op, op.minus(value, halfRange), op.add(value, halfRange));
	}

	private ConfidenceInterval(NumberOperations<N> op, N low, N high) {
		this.low = low;
		this.high = high;
		this.predicate = (v) -> op.gte(v, low) && op.lte(v, high);
	}

	public boolean contains(N value) {
		return predicate.test(value);
	}

	@Override
	public boolean test(N value) {
		return value != null && contains(value);
	}
}
