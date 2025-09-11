package org.meeuw.math.abstractalgebra.trivial;

import java.io.Serializable;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.AbelianRing;
import org.meeuw.math.abstractalgebra.AbelianRingElement;

/**
 * since 0.19
 */
public enum OddEven  implements AbelianRingElement<OddEven>, Serializable {

    odd,
    even;

    @Override
    public @NonNull AbelianRing<OddEven> getStructure() {
        return OddEventRing.INSTANCE;
    }

    @Override
    public OddEven times(OddEven multiplier) {
        return switch (this) {
            case odd -> multiplier;
            case even -> even;
        };
    }

    @Override
    public OddEven plus(OddEven summand) {
        return switch (this) {
            case odd -> switch (summand) {
                case odd -> even;
                case even -> odd;
            };
            case even -> summand;
        };
    }

    @Override
    public OddEven negation() {
        return switch(this) {
            case odd -> odd;
            case even -> even;
        };
    }
}
