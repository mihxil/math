package org.meeuw.math.text.spi;

import lombok.Generated;
import lombok.extern.java.Log;

import java.text.Format;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.meeuw.configuration.*;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

import static org.meeuw.configuration.ConfigurationService.getConfiguration;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public final class FormatService {

    private FormatService() {
    }


    /**
     * @param object an algebraic element for which a {@code Format} is needed
     * @param configuration an object to configure these instances
     * @return all available {@link Format} instances that would be available for the given algebraic element
     */
    public static Stream<Format> getFormat(AlgebraicElement<?> object, Configuration configuration) {
        return getFormat((Class<? extends AlgebraicElement<?>>) object.getClass(), configuration);
    }

    public static Stream<Format> getFormat(Class<? extends AlgebraicElement<?>> elementClass, Configuration configuration) {
        final List<AlgebraicElementFormatProvider> list = new ArrayList<>();
        getProviders().forEach(list::add);
        list.removeIf(e -> e.weight(elementClass) < 0);
        list.sort(Comparator.comparingInt(e -> -1 * e.weight(elementClass)));
        return list.stream().map(p -> p.getInstance(configuration));
    }

    public static Stream<AlgebraicElementFormatProvider> getProviders() {
        final ServiceLoader<AlgebraicElementFormatProvider> loader =
            ServiceLoader.load(AlgebraicElementFormatProvider.class);

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(loader.iterator(), Spliterator.ORDERED), false);
    }

    /**
     * @param object the element to creaate an string representation for
     * @return string representation of the given algebraic element.
     */
    public static String toString(AlgebraicElement<?> object) {
        return toString(object, getConfiguration());
    }

    public static String toString(AlgebraicElement<?> object, Configuration configuration) {
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

    public static <E extends AlgebraicElement<E>> E fromString(final String source, Class<E> clazz) {
        return fromString(source, clazz, getConfiguration());
    }


    public static <E extends AlgebraicElement<E>> E fromString(final String source, Class<E> clazz, Configuration configuration) {
        return getFormat(clazz, configuration)
            .map(f -> {
                try {
                    return (E) f.parseObject(source);
                } catch (ParseException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Could not parse '" + source + "' to " + clazz));
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Generated // exclude from coverage
    public static void setConfiguration(Configuration build) {
        ConfigurationService.setConfiguration(build);
    }
    @Deprecated
    @Generated
    public static <E extends ConfigurationAspect> void with(Class<E> configurationAspect, UnaryOperator<E> aspect, Runnable r) {
        ConfigurationService.withAspect(configurationAspect, aspect, r);
    }

    @Deprecated
    @Generated
    public static void with(Consumer<Configuration.Builder> configuration, Runnable r) {
        ConfigurationService.withConfiguration(configuration, r);
    }


}


