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
package org.meeuw.test.math.abstractalgebra.dim2;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.meeuw.math.abstractalgebra.dim2.Rotation2;
import org.meeuw.math.abstractalgebra.dim2.Rotation2Group;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.theories.abstractalgebra.MultiplicativeGroupTheory;

import static org.meeuw.math.Utils.Math_2PI;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class Rotation2Test implements MultiplicativeGroupTheory<Rotation2<UncertainReal>> {

    @Override
    public Arbitrary<Rotation2<UncertainReal>> elements() {
        return Arbitraries.doubles().ofScale(20).between(0, Math_2PI)
            .map(t -> Rotation2Group.rotationVector(element(t)));

    }
}
