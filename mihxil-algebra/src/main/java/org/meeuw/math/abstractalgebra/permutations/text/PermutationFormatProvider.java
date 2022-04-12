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
package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.Format;

import org.meeuw.configuration.Configuration;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.permutations.Permutation;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public Format getInstance(Configuration configuration) {
        PermutationConfiguration conf = configuration.getAspect(PermutationConfiguration.class);
        return new PermutationFormat(conf.getNotation(), conf.getOffset());
    }

    @Override
    public int weight(Class<? extends AlgebraicElement<?>> element) {
        return Permutation.class.isAssignableFrom(element) ? 1 : -1;
    }


}
