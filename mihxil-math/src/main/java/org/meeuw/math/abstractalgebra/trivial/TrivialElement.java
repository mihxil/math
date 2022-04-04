package org.meeuw.math.abstractalgebra.trivial;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.GroupElement;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public enum TrivialElement implements
    GroupElement<TrivialElement>,
    Serializable {

    e;

    @Override
    public TrivialGroup getStructure() {
        return TrivialGroup.INSTANCE;
    }

    @Override
    public TrivialElement operate(TrivialElement operand) {
        return this;
    }

    @Override
    public TrivialElement inverse() {
        return this;
    }

}
