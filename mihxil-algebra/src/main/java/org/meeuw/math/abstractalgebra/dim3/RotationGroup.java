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
package org.meeuw.math.abstractalgebra.dim3;

import java.util.Random;

import org.meeuw.math.Equivalence;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;

import static org.meeuw.math.Utils.Math_2PI;

/**
 * SO(3) group. A non-abelian multiplicative group.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RotationGroup extends AbstractAlgebraicStructure<Rotation> implements MultiplicativeGroup<Rotation> {

    public static final RotationGroup INSTANCE = new RotationGroup();

    private RotationGroup() {
        super(Rotation.class);
    }

    @Override
    public Rotation one() {
        return Rotation.ONE;
    }

    @Override
     public Rotation nextRandom(Random r) {
        Rotation rx = Rotation.Rx(r.nextDouble() * Math_2PI);
        Rotation ry = Rotation.Ry(r.nextDouble() * Math_2PI);
        Rotation rz = Rotation.Rz(r.nextDouble() * Math_2PI);
        return rx.times(ry).times(rz);
    }

    @Override
    public Cardinality getCardinality() {
        return RealField.INSTANCE.getCardinality();
    }

    @Override
    public Equivalence<Rotation> getEquivalence() {
        return (e1, e2) -> e1.rot.getStructure().getEquivalence().test(e1.rot, e2.rot);
    }

    @Override
    public String toString() {
        return "SO(3)";
    }
}
