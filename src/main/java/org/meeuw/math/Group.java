package org.meeuw.math;

/**
 * The algebraic structure 'group'. The operation is multiplication
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Group<F extends Group<F>> {

    F times(F f);

    F pow(int e);

    default F inverse() {
        return pow(-1);
    }

    default F dividedBy(F f) {
        return times(f.inverse());
    }

}
