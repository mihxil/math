package org.meeuw.math.abstractalgebra.integers;

import jakarta.validation.constraints.Min;

import java.math.BigInteger;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.numbers.Scalar;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class IntegerElement
    extends AbstractIntegerElement<IntegerElement, IntegerElement, Integers>
    implements
    RingElement<IntegerElement>,
    Scalar<IntegerElement>,
    MultiplicativeMonoidElement<IntegerElement>,
    GroupElement<IntegerElement>,
    Ordered<IntegerElement>,
    Factoriable<IntegerElement> {

    public static final IntegerElement ZERO = IntegerElement.of(0);
    public static final IntegerElement ONE = IntegerElement.of(1);


    public static IntegerElement of(long value){
        return new IntegerElement(value);
    }

    public IntegerElement(long value) {
        this(BigInteger.valueOf(value));
    }

    public IntegerElement(BigInteger value) {
        super(Integers.INSTANCE, value);
    }

    @Override
    public IntegerElement plus(IntegerElement summand) {
        return with(value.add(summand.value));
    }

    @Override
    public IntegerElement minus(IntegerElement summand) {
        return plus(summand.negation());
    }

    @Override
    public IntegerElement repeatedPlus(int multiplier) {
        return with(value.multiply(BigInteger.valueOf(multiplier)));
    }

    @Override
    public IntegerElement times(IntegerElement multiplier) {
        return with(value.multiply(multiplier.value));
    }

    @Override
    public IntegerElement pow(@Min(1) int n) {
        return with(Utils.positivePow(value, n));
    }

    @Override
    public IntegerElement factorial() {
        return new IntegerElement(bigIntegerFactorial());
    }

    @Override
    public IntegerElement sqr() {
        return with(value.multiply(value));
    }

    /**
     * Euclidean division of integers.
     * @param divisor integer divisor
     * @return this / divisor
     */
    public IntegerElement dividedBy(IntegerElement divisor) {
        return with(value.divide(divisor.value));
    }

    /**
     * The remainder of euclidean division of integers.
     * @param divisor integer divisor
     * @return this % divisor
     */
    public IntegerElement mod(IntegerElement divisor) {
        return with(value.remainder(divisor.value));
    }

    @Override
    public IntegerElement negation() {
        return with(value.negate());
    }

    @Override
    public IntegerElement abs() {
        return with(value.abs());
    }

    @Override
    public IntegerElement operate(IntegerElement operand) {
        return plus(operand);
    }

    @Override
    public IntegerElement inverse() {
        return negation();
    }
}
