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
package org.meeuw.math.abstractalgebra.permutations.text;

import lombok.Getter;
import lombok.With;

import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.permutations.Permutation;

/**
 * Aspects of configuration how a {@link Permutation} can be represented as a string
 * @author Michiel Meeuwissen
 * @since 0.4
 * @see #notation
 * @see #offset
 */
@Getter
public class PermutationConfiguration implements ConfigurationAspect {


    /**
     * The {@link Notation notation} to use when presenting a {@link Permutation}
     */
    @With
    private final Notation notation;

    /**
     * The number of the smallest element in the permutation. Traditionally {@link Offset#ONE}, and this is the default.
     * Java programmers may prefer {@link Offset#ONE}
     */
    @With
    private final Offset offset;


    public PermutationConfiguration() {
        this(Notation.LIST, Offset.ONE);
    }

    @lombok.Builder
    private PermutationConfiguration(Notation notation, Offset offset) {
        this.notation = notation;
        this.offset = offset;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(PermutationFormatProvider.class);
    }
}
