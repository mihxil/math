package org.meeuw.math.abstractalgebra.permutations.text;

import java.text.Format;
import java.util.Arrays;
import java.util.List;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.permutations.Permutation;
import org.meeuw.math.text.configuration.Configuration;
import org.meeuw.math.text.configuration.ConfigurationService;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class PermutationFormatProvider extends AlgebraicElementFormatProvider {

    @Override
    public Format getInstance(ConfigurationService configuration) {
        PermutationConfiguration conf = configuration.get(PermutationConfiguration.class);
        return new PermutationFormat(conf.getNotation(), conf.getOffset());
    }

    @Override
    public int weight(AlgebraicElement<?> element) {
        return element instanceof Permutation ? 1 : -1;
    }

    @Override
    public List<Configuration> getConfigurationSettings() {
        return Arrays.asList(PermutationConfiguration.builder()
            .notation(Notation.CYCLES)
            .offset(Offset.ONE)
            .build());
    }

}
