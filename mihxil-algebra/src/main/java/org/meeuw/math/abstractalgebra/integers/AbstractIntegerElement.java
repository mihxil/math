package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.exceptions.InvalidFactorial;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.numbers.*;

/**
 * The abstract element belonging to {@link AbstractIntegers}
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <E> self reference
 * @param <SIZE> the type of the absolute value
 * @param <S> the type of the structure
 *
 */
public abstract class AbstractIntegerElement<
    E extends AbstractIntegerElement<E, SIZE, S>,
    SIZE extends Scalar<SIZE>,
    S extends AbstractIntegers<E, SIZE, S>
    >
    extends AbstractAlgebraicElement<E, S> implements
    SignedNumber<E>,
    AlgebraicElement<E>,
    SizeableScalar<E, SIZE>,
    Ordered<E> {

    /**
     * {@link BigInteger#TWO}, but for java 8.
     */
    static final BigInteger BigTWO = BigInteger.valueOf(2);

    @Getter
    protected final BigInteger value;

    /**
     * The constructor, which initializes the backing {@link BigInteger}.
     * This performs no checking on this input, and should therefore remain protected.
     * It is only called when it is sure beforehand that this will result a new value belonging to the given structure.
     *
     * Otherwise calls {@link AbstractIntegers#newElement(BigInteger)}
     */
    protected AbstractIntegerElement(S structure, BigInteger value) {
        super(structure);
        this.value = value;
    }

    /**
     * This is a protected shorthand for creating new elements.
     */
    protected final E with(BigInteger value) {
        return structure.of(value);
    }

    public E pow(int n) {
        try {
            return with(value.pow(n));
        } catch (ArithmeticException ae) {
            throw new ReciprocalException(ae);
        }
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value.toString());
    }

    @Override
    public BigInteger bigIntegerValue() {
        return value;
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    public int compareTo(E f) {
        return value.compareTo(f.value);
    }

    @Override
    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    protected BigInteger bigIntegerFactorial()  {
        if (value.signum() == -1) {
            throw new InvalidFactorial("Cannot take factorial of negative integer");
        }
        Long maxArgument = ConfigurationService.getConfigurationAspect(Factoriable.Configuration.class).getMaxArgument();
        if (maxArgument != null && value.intValue() > maxArgument) {
            throw new InvalidFactorial("Factorial too big (" + value.intValue() + ">" + maxArgument + ")");
        }
        BigInteger product = BigInteger.ONE;
        for (BigInteger i = BigInteger.ONE; i.compareTo(value) <= 0; i = i.add(BigInteger.ONE)) {
            product = product.multiply(i);
        }
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        E that = (E) o;

        return value.equals(that.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends AlgebraicElement<F>> Optional<F> castDirectly(Class<F> clazz) {
        if (clazz.isAssignableFrom(IntegerElement.class)) {
            return Optional.of((F) new IntegerElement(getValue()));
        }
        if (clazz.isAssignableFrom(RationalNumber.class)) {
            return Optional.of((F) RationalNumber.of(getValue(), BigInteger.ONE));
        }
        return Optional.empty();
    }

}
