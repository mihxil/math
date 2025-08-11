/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.physics;

import lombok.Getter;

import java.util.function.BiFunction;

import org.checkerframework.checker.units.qual.*;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.reals.RealField;

import static org.meeuw.physics.Dimension.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public enum SIUnit implements BaseUnit {

    @m m(L, "meter"),
    @kg kg(M, "kilogram", (prefix, siunit) -> {
        if (prefix == SI.DecimalPrefix.k) {
            return siunit;
        } else {
            return SI.g.withPrefix(prefix);
        }
    }),
    @s s(T, "second"),
    @A A(I, "ampere"),
    @K K(TH, "kelvin"),
    @mol mol(N, "mole"),
    @cd cd(J, "candela")

    ;

    static final int NUMBER = values().length;

    static {
        assert NUMBER == Dimension.NUMBER;
        for (SIUnit v : values()){
            assert v.ordinal() == v.dimension.ordinal();
        }
    }

    @Getter
    private final Dimension dimension;

    @Getter
    private final String description;

    private final BiFunction<Prefix, SIUnit, Unit> withPrefixImpl;

    SIUnit(Dimension dimension, String description) {
        this(dimension, description, null);
    }

    SIUnit(Dimension dimension, String description, BiFunction<Prefix, SIUnit, Unit> withPrefixImpl) {
        this.dimension = dimension;
        this.description = description;
        this.withPrefixImpl = withPrefixImpl;
    }

    @Override
    public Unit withPrefix(Prefix prefix) {
        return withPrefixImpl == null ? BaseUnit.super.withPrefix(prefix) :
            withPrefixImpl.apply(prefix, this);
    }

    @Override
    public DimensionalAnalysis getDimensions() {
        return getDimensions(ordinal());
    }

    @Override
    public SystemOfMeasurements getSystem() {
        return SI.INSTANCE;
    }




    private static DimensionalAnalysis getDimensions(int ord) {
        int[] exponents = new int[7];
        exponents[ord] = 1;
        return new DimensionalAnalysis(exponents);
    }

    public static Units toUnits(int[] exponents) {
        UnitExponent[] unitExponents =  new UnitExponent[Dimension.values().length];
        for (int i = 0; i < exponents.length; i++) {
            unitExponents[i] = UnitExponent.of(values()[i], exponents[i]);
        }
        return new CompositeUnits(RealField.INSTANCE.one(), unitExponents);
    }

    @Override
    public RealNumber getSIFactor() {
        return RealField.INSTANCE.one();
    }

    public static SIUnit valueOf(Dimension dimension) {
        for (SIUnit si :  SIUnit.values()) {
            if (si.dimension == dimension) {
                return si;
            }
        }
        throw new IllegalArgumentException();
    }


}
