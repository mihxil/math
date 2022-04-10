package org.meeuw.math.abstractalgebra.quaternions;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.FieldInCompleteException;
import org.meeuw.math.numbers.Sizeable;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class Quaternion<E extends ScalarFieldElement<E>>
    implements
    DivisionRingElement<Quaternion<E>>,
    Sizeable<E>,
    Serializable,
    WithScalarOperations<Quaternion<E>, E> {

    static final long serialVersionUID = 0L;

    @Getter
    final E a;
    @Getter
    final E b;
    @Getter
    final E c;
    @Getter
    final E d;

    public Quaternion(E a, E b, E c, E d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public Quaternions<E> getStructure() {
        return Quaternions.of(a.getStructure());
    }

    /**
     * Hamilton product
     */
    @Override
    public Quaternion<E> times(Quaternion<E> multiplier) {
        return new Quaternion<>(
            a.times(multiplier.a).minus(b.times(multiplier.b)).minus(c.times(multiplier.c)).minus(d.times(multiplier.d)),
            a.times(multiplier.b).plus(b.times(multiplier.a)).plus(c.times(multiplier.d)).minus(d.times(multiplier.c)),
            a.times(multiplier.c).minus(b.times(multiplier.d)).plus(c.times(multiplier.a)).plus(d.times(multiplier.b)),
            a.times(multiplier.d).plus(b.times(multiplier.c)).minus(c.times(multiplier.b)).plus(d.times(multiplier.a))
        );
    }

    @Override
    public Quaternion<E> times(E multiplier) {
        return new Quaternion<>(
            a.times(multiplier),
            b.times(multiplier),
            c.times(multiplier),
            d.times(multiplier)
        );
    }

    @Override
    @NonAlgebraic
    public Quaternion<E> dividedBy(Quaternion<E> divisor) throws DivisionByZeroException {
        return DivisionRingElement.super.dividedBy(divisor);
    }

    @Override
    public Quaternion<E> dividedBy(E divisor) {
        return new Quaternion<>(
            a.dividedBy(divisor),
            b.dividedBy(divisor),
            c.dividedBy(divisor),
            d.dividedBy(divisor)
        );
    }

    @Override
    public Quaternion<E> reciprocal() {
        E divisor = a.sqr().plus(b.sqr()).plus(c.sqr()).plus(d.sqr());
        return new Quaternion<>(
            a.dividedBy(divisor),
            b.negation().dividedBy(divisor),
            c.negation().dividedBy(divisor),
            d.negation().dividedBy(divisor)
        );
    }

    @Override
    public Quaternion<E> plus(Quaternion<E> summand) {
         return new Quaternion<>(
             a.plus(summand.a),
             b.plus(summand.b),
             c.plus(summand.c),
             d.plus(summand.d)
         );
    }

    @Override
    public Quaternion<E> negation() {
        return new Quaternion<>(
            a.negation(),
            b.negation(),
            c.negation(),
            d.negation()
        );
    }

    public Quaternion<E> conjugate() {
        return new Quaternion<>(
            a,
            b.negation(),
            c.negation(),
            d.negation()
        );
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @NonAlgebraic
    public E abs() {
        if (getStructure().getElementStructure() instanceof CompleteField) {
            return (E) ((CompleteFieldElement) (a.sqr().plus(b.sqr()).plus(c.sqr()).plus(d.sqr()))).sqrt();
        } else {
            throw new FieldInCompleteException("Field of " + this + " is not complete");
        }
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (! a.isZero()) {
            result.append(a.toString());
        }
        append(b, result, 'i');
        append(c, result, 'j');
        append(d, result, 'k');
        if (result.length() == 0) {
            result.append('0');
        }
        return result.toString();
    }


    protected void append(E value, StringBuilder result, char i) {
        boolean hasValues = result.length() > 0;
        if (!value.isZero()) {
            if (hasValues) {
                result.append(' ');
            }
            if (value.isNegative()) {
                result.append('-');
            } else {
                if (hasValues) {
                    result.append('+');
                }
            }
            if (hasValues) {
                result.append(' ');
            }
            E abs = value.abs();
            if (! abs.isOne()) {
                result.append(abs.toString());
            }
            result.append(i);
        }
    }

}
