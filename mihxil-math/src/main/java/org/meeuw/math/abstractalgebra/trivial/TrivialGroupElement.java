package org.meeuw.math.abstractalgebra.trivial;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.GroupElement;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public enum TrivialGroupElement implements
    GroupElement<TrivialGroupElement>,
    Serializable {

    e;

    @Override
    public TrivialGroup getStructure() {
        return TrivialGroup.INSTANCE;
    }

    @Override
    public TrivialGroupElement operate(TrivialGroupElement operand) {
        return this;
    }

    @Override
    public TrivialGroupElement inverse() {
        return this;
    }

}
