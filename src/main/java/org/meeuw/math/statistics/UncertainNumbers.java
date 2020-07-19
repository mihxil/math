package org.meeuw.math.statistics;


import org.meeuw.math.abstractalgebra.Field;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UncertainNumbers implements Field<UncertainNumber<?>, UncertainNumbers> {

    public static UncertainNumbers INSTANCE = new UncertainNumbers();

    private UncertainNumbers() {

    }


    @Override
    public ImmutableUncertainNumber zero() {
        return new ImmutableUncertainNumber(0, 0);

    }

    @Override
    public ImmutableUncertainNumber one() {
        return new ImmutableUncertainNumber(1d, 0);
    }
}
