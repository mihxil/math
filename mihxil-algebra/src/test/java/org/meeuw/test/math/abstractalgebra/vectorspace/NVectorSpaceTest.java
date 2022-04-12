/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.abstractalgebra.vectorspace;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.vectorspace.NVectorSpace;
import org.meeuw.math.exceptions.NotStreamable;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NVectorSpaceTest {

    @Test
    public void nonstreamable() {
        NVectorSpace<RealNumber> realSpace = NVectorSpace.of(3, RealField.INSTANCE);
        assertThatThrownBy(realSpace::stream).isInstanceOf(NotStreamable.class);
    }
}
