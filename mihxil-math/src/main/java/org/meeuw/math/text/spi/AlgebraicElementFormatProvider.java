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

    public abstract Format getInstance(int minimumExponent);

    public abstract int weight(AlgebraicElement<?> weight);

    public static Stream<Format> getFormat(AlgebraicElement<?> object, int minimumExponent ) {
        final ServiceLoader<AlgebraicElementFormatProvider> loader = ServiceLoader.load(AlgebraicElementFormatProvider.class);
        List<AlgebraicElementFormatProvider> list = new ArrayList<>();
        loader.iterator().forEachRemaining(list::add);
        list.sort(Comparator.comparingInt(e -> e.weight(object)));

        return list.stream().map(p -> p.getInstance(minimumExponent));
    }

    public static String toString(AlgebraicElement<?> object, int minimumExponent ) {
        return getFormat(object, minimumExponent)
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
