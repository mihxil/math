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
package org.meeuw.math.abstractalgebra.reals;

import java.util.*;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.ComplexNumbers;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;

/**
 * The field of real numbers.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(CompleteScalarField.class)
@Singleton
public class RealField extends AbstractAlgebraicStructure<RealNumber>
    implements CompleteScalarField<RealNumber>, MetricSpace<RealNumber, RealNumber> {

    public static final RealField INSTANCE = new RealField();

    private RealField() {
        super(RealNumber.class);
    }

    @Override
    public RealNumber zero() {
        return RealNumber.ZERO;
    }

    @Override
    public RealNumber one() {
        return RealNumber.ONE;
    }

    public RealNumber two() {
        return RealNumber.TWO;
    }

     @Override
    public RealNumber nextRandom(Random random) {
        return RealNumber.of(random.nextDouble());
    }


    @Override
    public Cardinality getCardinality() {
        return Cardinality.C;
    }

    @Override
    public Set<AlgebraicStructure<?>> getSuperGroups() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            ComplexNumbers.INSTANCE
        )));
    }

    @Override
    public String toString() {
        return "ℝ" + TextUtils.subscript("p"); // 'backed by primitive'
    }

    public RealNumber considerMultiplicationBySpecialValues(RealNumber r1, RealNumber r2) {
        double newValue = r1.value * r2.value;
        // multiplication by zero
        if (r1.isExactlyZero() || r2.isExactlyZero()) {
            return zero();
        } else if (r1.value == 0 && r2.value != 0) {
            return new RealNumber(newValue, r1.uncertainty);
        } else if (r2.value == 0 && r1.value != 0) {
            return new RealNumber(newValue, r2.uncertainty);
            // NaN
        } else if (Double.isNaN(r1.value) || Double.isNaN(r2.value)) {
            return new RealNumber(newValue, Double.NaN);
        } else {
            return new RealNumber(newValue,
                Math.abs(newValue) * (
                    Math.abs(r1.uncertainty / (r1.value + r1.uncertainty)) +
                        Math.abs(r2.uncertainty / (r2.value + r2.uncertainty))) + uncertaintyForDouble(newValue)

            );
        }
    }

    public RealNumber atan2(RealNumber y, RealNumber x) {
        double value = Math.atan2(y.value, x.value);
        return new RealNumber(
            Math.atan2(y.value, x.value),
            uncertaintyForDouble(value) // TODO
        );
    }


    @Override
    public RealNumber pi() {
        return RealNumber.of(Math.PI);
    }

    @Override
    public RealNumber e() {
        return RealNumber.of(Math.E);
    }
    @Override
    public RealNumber 𝜑() {
        return RealNumber.of((1 + Math.sqrt(5)) / 2);
    }
}
