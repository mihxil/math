package org.meeuw.math.abstractalgebra.klein;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.Group;
import org.meeuw.math.abstractalgebra.GroupElement;

/**
 * @since 0.8
 */
public enum KleinElement implements GroupElement<KleinElement> {

    e() {
        @Override
        public KleinElement operate(KleinElement multiplier) {
            return multiplier;
        }
    },
    a,
    b,
    c
    ;

    @Override
    public Group<KleinElement> getStructure() {
        return KleinGroup.INSTANCE;
    }

    @Override
    public KleinElement operate(KleinElement multiplier) {
        if (multiplier == e) {
            return this;
        }
        if (multiplier == this) {
            return e;
        }
        return Stream.of(a, b, c)
            .filter(el -> el != this && el != multiplier)
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }

    @Override
    public KleinElement inverse() {
        return this;
    }


}
