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
package org.meeuw.math.abstractalgebra;

import lombok.Getter;
import lombok.With;

import java.util.Arrays;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

/**
 * Groups are associated with a binary operator. This operator may have several interpretations.
 * <p>
 * Some groups support more than one binary operator (e.g. both addition and multiplication), and in that case we can configure which of those should be associated with the generic {@link BasicAlgebraicBinaryOperator#OPERATION}
 */
public class GenericGroupConfiguration implements ConfigurationAspect {


    @With
    @Getter
    private final BasicAlgebraicBinaryOperator groupOperator;

    public GenericGroupConfiguration() {
        this(BasicAlgebraicBinaryOperator.MULTIPLICATION);
    }

    @lombok.Builder
    private GenericGroupConfiguration(BasicAlgebraicBinaryOperator groupOperator) {
        this.groupOperator = groupOperator;
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Arrays.asList(Group.class);
    }
}
