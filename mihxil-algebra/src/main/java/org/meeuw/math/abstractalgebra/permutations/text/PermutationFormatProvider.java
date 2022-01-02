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
