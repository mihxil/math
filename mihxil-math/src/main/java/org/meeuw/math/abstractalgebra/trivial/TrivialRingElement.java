package org.meeuw.math.abstractalgebra.trivial;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.RingElement;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public enum TrivialRingElement implements
    RingElement<TrivialRingElement>,
    Serializable {

    e;

    @Override
    public TrivialRing getStructure() {
        return TrivialRing.INSTANCE;
    }

    @Override
    public TrivialRingElement times(TrivialRingElement multiplier) {
        return this;
    }

    @Override
    public TrivialRingElement plus(TrivialRingElement summand) {
        return this;
    }

    @Override
    public TrivialRingElement negation() {
        return this;
    }

    @Override
    public TrivialRingElement operate(TrivialRingElement operand) {
        return this;
    }

    @Override
    public TrivialRingElement inverse() {
        return this;
    }

}
