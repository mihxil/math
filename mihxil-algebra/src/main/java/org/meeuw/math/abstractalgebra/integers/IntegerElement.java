package org.meeuw.math.abstractalgebra.integers;

import javax.validation.constraints.Min;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.numbers.Scalar;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class IntegerElement
    extends AbstractIntegerElement<IntegerElement, IntegerElement>
    implements
    RingElement<IntegerElement>,
    Scalar<IntegerElement>,
    MultiplicativeMonoidElement<IntegerElement>,
    Ordered<IntegerElement> {

    public static final IntegerElement ZERO = IntegerElement.of(0);
    public static final IntegerElement ONE = IntegerElement.of(1);


    public static IntegerElement of(long value){
        return new IntegerElement(value);
    }

    public IntegerElement(long value) {
        super(value);
    }

    @Override
    public IntegerElement plus(IntegerElement summand) {
        return new IntegerElement(value + summand.value);
    }

    @Override
    public IntegerElement minus(IntegerElement summand) {
        return plus(summand.negation());
    }

    @Override
    public IntegerElement repeatedPlus(int multiplier) {
        return new IntegerElement(value * multiplier);
    }

    @Override
    public Integers getStructure() {
        return Integers.INSTANCE;
    }

    @Override
    public IntegerElement times(IntegerElement multiplier) {
        return new IntegerElement(value * multiplier.value);
    }

    @Override
    public IntegerElement pow(@Min(1) int n) {
        return new IntegerElement(Utils.positivePow(value, n));
    }

    @Override
    public IntegerElement sqr() {
        return new IntegerElement(value * value);
    }

    /**
     * Euclidean division of integers.
     * @param divisor integer divisor
     * @return this / divisor
     */
    public IntegerElement dividedBy(IntegerElement divisor) {
        return new IntegerElement(value / divisor.value);
    }

    /**
     * The remainder of euclidean division of integers.
     * @param divisor integer divisor
     * @return this % divisor
     */
     public IntegerElement mod(IntegerElement divisor) {
        return new IntegerElement(value % divisor.value);
    }

    @Override
    public int compareTo(IntegerElement o) {
        return Long.compare(value, o.value);
    }

    @Override
    public IntegerElement negation() {
        return new IntegerElement(-1 * value);
    }

    @Override
    public IntegerElement abs() {
        return new IntegerElement(Math.abs(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerElement that = (IntegerElement) o;

        return value == that.value;
    }

}
