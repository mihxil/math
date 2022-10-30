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
package org.meeuw.test.math.abstractalgebra.permutations.text;

import java.text.ParseException;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.permutations.Permutation;
import org.meeuw.math.abstractalgebra.permutations.text.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
class PermutationFormatTest {

    @Test
    public void aspect() {
        List<ConfigurationAspect> check = ConfigurationService.getConfiguration()
            .getConfigurationAspectsAssociatedWith(PermutationFormatProvider.class);

        assertThat(check).hasSize(1);
        assertThat(check.get(0)).isInstanceOf(PermutationConfiguration.class);
        assertThat(((PermutationConfiguration) check.get(0)).getOffset()).isEqualTo(Offset.ONE);


    }

    @Test
    public void format() {

        final PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        assertThatThrownBy(() -> format.format(new Object())).isInstanceOf(IllegalArgumentException.class);

        Permutation permutation = Permutation.of(2, 1, 4, 3);
        assertThat(format.format(permutation)).isEqualTo("(2143)");
        PermutationFormat cycleFormat = format.withNotation(Notation.CYCLES).withOffset(Offset.ZERO);
        assertThat(cycleFormat.format(permutation)).isEqualTo("(01)(23)");
    }


    @Test
    void parseObject() throws ParseException {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(321)");
        assertThat(perm.permute("a", "b", "c")).containsExactly("c", "b", "a");
    }

    @Test
    void parseObjectWithSpaces() throws ParseException {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(3 2 1)");
        assertThat(perm.permute("a", "b", "c")).containsExactly("c", "b", "a");
    }

    @Test
    void parseObjectShort() throws ParseException {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(1)");
        assertThat(perm.permute("a", "c")).containsExactly("a", "c");
    }

    @Test
    void invalidParse() {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        assertThatThrownBy(() -> format.parseObject("(453")).isInstanceOf(java.text.ParseException.class);

        assertThatThrownBy(() -> format.parseObject("453)")).isInstanceOf(java.text.ParseException.class);


        assertThatThrownBy(() -> format.parseObject("(456)")).isInstanceOf(InvalidElementCreationException.class);


    }

}
