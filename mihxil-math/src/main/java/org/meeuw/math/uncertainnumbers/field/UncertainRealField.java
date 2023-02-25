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
package org.meeuw.math.uncertainnumbers.field;


import java.util.Random;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;

/**
 * The field of {@link UncertainReal}'s
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(CompleteScalarField.class)
public class UncertainRealField
    extends AbstractAlgebraicStructure<UncertainReal>
    implements CompleteScalarField<UncertainReal> {

    public static final UncertainRealField INSTANCE = new UncertainRealField();

    private UncertainRealField() {
        super(UncertainReal.class);
    }

    @Override
    public UncertainReal zero() {
        return UncertainDoubleElement.ZERO;
    }

    @Override
    public UncertainReal one() {
        return UncertainDoubleElement.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public UncertainReal nextRandom(Random random) {
        double value = ConfigurationService.getConfigurationAspect(RandomConfiguration.class).getSetSize() * (random.nextDouble() - 0.5d);
        boolean nan = random.nextDouble() < 0.1;
        return new UncertainDoubleElement(value,
            nan ? Double.NaN : Math.abs(0.5 * value * random.nextDouble())
        );
    }

    @Override
    public String toString() {
        return "ℝᵤ"; // ᵤ: uncertain values
    }

    @Override
    public UncertainReal pi() {
        return UncertainDoubleElement.of(Math.PI, DoubleUtils.uncertaintyForDouble(Math.PI));
    }

    @Override
    public UncertainReal e() {
        return UncertainDoubleElement.of(Math.E, DoubleUtils.uncertaintyForDouble(Math.E));
    }

}
