package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.Format;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.permutations.Permutation;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.text.spi.Configuration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public Format getInstance(Configuration configuration) {
        Notation notation = configuration.getPropertyOrDefault(Notation.class.getName(), Notation.CYCLES);
        Offset offset = configuration.getPropertyOrDefault(Offset.class.getName(), Offset.ONE);
        return new PermutationFormat(notation, offset);
    }

    @Override
    public int weight(AlgebraicElement<?> element) {
        return element instanceof Permutation ? 1 : -1;
    }
}
