package org.meeuw.math.text.spi;

import java.text.Format;
import java.util.*;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AlgebraicElementFormatProvider {

    public abstract Format getInstance(Configuration configuration);

    public abstract int weight(AlgebraicElement<?> weight);

    public static <C extends Configuration> Stream<Format> getFormat(AlgebraicElement<?> object, Configuration configuration ) {
        final ServiceLoader<AlgebraicElementFormatProvider> loader = ServiceLoader.load(AlgebraicElementFormatProvider.class);
        List<AlgebraicElementFormatProvider> list = new ArrayList<>();
        loader.iterator().forEachRemaining(list::add);
        list.sort(Comparator.comparingInt(e -> -1 * e.weight(object)));

        return list.stream().map(p -> p.getInstance(configuration));
    }

    public static String toString(AlgebraicElement<?> object) {
        return toString(object, Configuration.get());
    }

    public static String toString(AlgebraicElement<?> object, Configuration configuration ) {
        return getFormat(object, configuration)
            .map(f -> {
                try {
                    return f.format(object);
                } catch (IllegalArgumentException iea) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("<TO STRING FAILED>");
    }
}
