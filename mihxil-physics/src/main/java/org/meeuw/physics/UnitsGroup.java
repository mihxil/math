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

import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.streams.StreamUtils;

import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;

/**
 * The {@link MultiplicativeAbelianGroup} of {@link Units}.
 * <p>
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Singleton
public class UnitsGroup extends AbstractAlgebraicStructure<Units> implements
    MultiplicativeAbelianGroup<Units>,
    Streamable<Units> {

    private static final Units ONE = CompositeUnits.DIMENSIONLESS;

    public static final UnitsGroup INSTANCE = new UnitsGroup();

    private UnitsGroup() {
        super(Units.class);
    }

    @Override
    public Units one() {
        return ONE;
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public Stream<Units> stream() {
        return StreamUtils.allIntArrayStream(SIUnit.values().length).map(
            array -> {
                UnitExponent[] units = new UnitExponent[array.length];
                for (int i = 0; i < array.length; i++) {
                    units[i] = new UnitExponent(SIUnit.values()[i], array[i]);
                }
                return new CompositeUnits(exactly(1), units);
            }
        );
    }

    @Override
    public Units nextRandom(Random random) {
        UnitExponent[] units = new UnitExponent[SIUnit.values().length];

        for (int i = 0; i < units.length; i++) {
            units[i] = new UnitExponent(SIUnit.values()[i],  (int) (random.nextGaussian() * 3));
        }
        return new CompositeUnits(exactly(1), units);

    }
}
