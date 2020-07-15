package org.meeuw.math;

/**
 * A group with a second operation. Addition.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<F extends Field<F>> extends Group<F> {

    F plus(F m);

    default F minus(F m) {
        return plus(m.negate());
    }

    F negate();

}
