package org.meeuw.math.text.spi;

import java.text.Format;
import java.util.*;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.configuration.ConfigurationService;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FormatServiceProvider {

    public static <C extends ConfigurationService> Stream<Format> getFormat(
        AlgebraicElement<?> object, ConfigurationService configuration ) {

        final ServiceLoader<AlgebraicElementFormatProvider> loader =
            ServiceLoader.load(AlgebraicElementFormatProvider.class);
        final List<AlgebraicElementFormatProvider> list = new ArrayList<>();
        loader.iterator().forEachRemaining(list::add);
        list.removeIf(e -> e.weight(object) < 0);
        list.sort(Comparator.comparingInt(e -> -1 * e.weight(object)));
        return list.stream().map(p -> p.getInstance(configuration));
    }


    public static String toString(AlgebraicElement<?> object) {
        return toString(object, ConfigurationService.get());
    }

    public static String toString(AlgebraicElement<?> object, ConfigurationService configuration ) {
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
