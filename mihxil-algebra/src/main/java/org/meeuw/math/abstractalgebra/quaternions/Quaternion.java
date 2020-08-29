package org.meeuw.math.abstractalgebra.quaternions;

import lombok.EqualsAndHashCode;

import lombok.Getter;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class Quaternion<E extends NumberFieldElement<E>>
    implements DivisionRingElement<Quaternion<E>> {

    @Getter
    private final E a;
    @Getter
    private final E b;
    @Getter
    private final E c;
    @Getter
    private final E d;

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
            a.times(multiplier.b).plus (b.times(multiplier.a)).plus (c.times(multiplier.d)).minus(d.times(multiplier.c)),
            a.times(multiplier.c).minus(b.times(multiplier.d)).plus (c.times(multiplier.a)).plus (d.times(multiplier.b)),
            a.times(multiplier.d).plus( b.times(multiplier.c)).minus(c.times(multiplier.b)).plus (d.times(multiplier.a))
        );
    }

    public Quaternion<E> times(E multiplier) {
        return new Quaternion<>(
            a.times(multiplier),
            b.times(multiplier),
            c.times(multiplier),
            d.times(multiplier)
        );
    }

    public Quaternion<E> dividedBy(E multiplier) {
        return new Quaternion<>(
            a.dividedBy(multiplier),
            b.dividedBy(multiplier),
            c.dividedBy(multiplier),
            d.dividedBy(multiplier)
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (! a.isZero()) {
            result.append(a.toString());
        }
        append(b, result, 'i');
        append(c, result, 'j');
        append(d, result, 'k');
        return result.toString();
    }

    protected void append(E imaginary, StringBuilder result, char i) {
        boolean hasValues = result.length() > 0;
        if (!imaginary.isZero()) {
            if (hasValues) {
                result.append(' ');
            }
            if (imaginary.isNegative()) {
                result.append('-');
            } else {
                if (hasValues) {
                    result.append('+');
                }
            }
            if (hasValues) {
                result.append(' ');
            }
            E abs = imaginary.abs();
            if (! abs.isOne()) {
                result.append(abs.toString());
            }
            result.append(i);
        }
    }


}
