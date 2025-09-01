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


import java.util.Random;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.FormatService;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;

/**
 * The field of {@link RealNumber}'s
 * <p>
 * It can have any kind of {@link RealNumber} elements. But it can always fall back to
 * {@link DoubleElement}, an immutable instance based on primitive doubles.
 * <h3>special values</h3>
 * <p>
 * Primitive {@code double} can contain special values {@link Double#NaN}, {@link Double#POSITIVE_INFINITY}, {@link Double#NEGATIVE_INFINITY}.
 * There are currently intentionally not supported, but I think I changed my mind a few times. May be it is worth supporting them, because
 * why would a {@code DoubleElement} be less 'powerful' then an {@code double}
 * <p>
 * But it gives some issues, when <em>comparing</em>. E.g. is 1 \pm Nan, equal to 0 \pm Nan? Is {@code +Infinity} a proper element of the group? It is not really. It doesn't have a proper {@link MultiplicativeGroupElement#reciprocal} or {@link AdditiveGroupElement#negation()}
 * <p>
 * For now, {@code NaN} uncertainties are accepted though.
 *
 * @author Michiel Meeuwissen
 * @see DoubleElement
 * @since 0.4
 */
@Example(CompleteScalarField.class)
@Singleton
public class RealField
    extends AbstractAlgebraicStructure<RealNumber>
    implements CompleteScalarField<RealNumber>,
    MetricSpace<RealNumber, RealNumber>
   {

    public static final RealField INSTANCE = new RealField();

    private RealField() {
        super(RealNumber.class);
    }

    @Override
    public RealNumber zero() {
        return DoubleElement.ZERO;
    }

    @Override
    public RealNumber one() {
        return DoubleElement.ONE;
    }

    public RealNumber two() {
        return DoubleElement.TWO;
    }
    /**
     * Just as {@link DoubleElement#exactly(double)}, but with a name more emphasizing that this creates an element of the field.
     * @since 0.15
     */
    public static DoubleElement element(double value) {
        return DoubleElement.exactly(value);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public DoubleElement nextRandom(Random random) {
        double value = ConfigurationService.getConfigurationAspect(RandomConfiguration.class).getSetSize() * (random.nextDouble() - 0.5d);
        boolean nan = random.nextDouble() < 0.1;
        return new DoubleElement(value,
            nan ? Double.NaN : Math.abs(0.05 * value * random.nextDouble())
        );
    }

    @Override
    public String toString() {
        return "ℝᵤ"; // ᵤ: uncertain values
    }

    @Override
    public RealNumber pi() {
        return DoubleElement.of(Math.PI, DoubleUtils.uncertaintyForDouble(Math.PI));
    }

    @Override
    public RealNumber e() {
        return DoubleElement.of(Math.E, DoubleUtils.uncertaintyForDouble(Math.E));
    }

    @Override
    public RealNumber 𝜑() {
        double phi = (1d + Math.sqrt(5)) / 2;
        return DoubleElement.of(phi, DoubleUtils.uncertaintyForDouble(phi));
    }

    @Override
    public RealNumber fromString(String s) {
        return FormatService.fromString(s,  RealNumber.class);
    }

    public RealNumber atan2(RealNumber y, RealNumber x) {
        double value = Math.atan2(y.doubleValue(), x.doubleValue());
        return DoubleElement.of(
            value,
            uncertaintyForDouble(value) // TODO
        );
    }






}
