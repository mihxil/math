package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.ParsePosition;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.permutations.Permutation;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class PermutationFormatTest {

    @Test
    void parseObject() {
        PermutationFormat format = new PermutationFormat(Notation.LIST, Offset.ONE);
        Permutation perm = (Permutation) format.parseObject("(321)", new ParsePosition(0));
        assertThat(perm.permute("a", "b", "c")).containsExactly("c", "b", "a");
    }
}
