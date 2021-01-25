package org.meeuw.math.text.spi;

import lombok.extern.java.Log;

import java.text.Format;
import java.util.*;
import java.util.function.*;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.FixedSizeMap;
import org.meeuw.math.text.configuration.Configuration;
import org.meeuw.math.text.configuration.ConfigurationAspect;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public final class FormatServiceProvider {

    private FormatServiceProvider() {
    }
    private static final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> INITIAL_MAP
        = Collections.unmodifiableMap(createConfigurationMap());

    private static final Configuration.Builder DEFAULT = Configuration.builder();

    private static final ThreadLocal<Configuration> CONFIGURATION =
        ThreadLocal.withInitial(DEFAULT::build);


    /**
     * Returns all available {@link Format} instances that would be available for the given algebraic element
     */
    public static Stream<Format> getFormat(AlgebraicElement<?> object, Configuration configuration) {
        final List<AlgebraicElementFormatProvider> list = new ArrayList<>();
        getProviders().forEach(list::add);
        list.removeIf(e -> e.weight(object) < 0);
        list.sort(Comparator.comparingInt(e -> -1 * e.weight(object)));
        return list.stream().map(p -> p.getInstance(configuration));
    }

    public static Stream<AlgebraicElementFormatProvider> getProviders() {
        final ServiceLoader<AlgebraicElementFormatProvider> loader =
            ServiceLoader.load(AlgebraicElementFormatProvider.class);

        Stream<AlgebraicElementFormatProvider> stream = StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(loader.iterator(), Spliterator.ORDERED), false);
        return stream;
    }

    /**
     *
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

    /**
     * Configures the default configuration object.
     */
    public static void defaultConfiguration(Consumer<Configuration.Builder> consumer) {
        consumer.accept(DEFAULT);
        CONFIGURATION.remove();
    }

    /**
     * @return the current configuration object
     */
    public  static Configuration getConfiguration() {
        return CONFIGURATION.get();
    }

    /**
     * @return an aspect of the the current configuration object
     * @param <E> The type of the aspect to obtain
     * @param clazz the class of the aspect to obtain
     */
    public static <E extends ConfigurationAspect> E getConfigurationAspect(Class<E> clazz) {
        return CONFIGURATION.get().get(clazz);
    }

    public static void with(Configuration configuration, Runnable r) {
        with(configuration, () -> {
            r.run();
            return null;
        });
    }

    public static <E extends ConfigurationAspect, R> R with(Class<E> configurationAspect, UnaryOperator<E> aspect, Supplier<R> r) {
        return with(getConfiguration().with(configurationAspect, aspect), r);
    }

    public static <E extends ConfigurationAspect> void with(Class<E> configurationAspect, UnaryOperator<E> aspect, Runnable r) {

        with(getConfiguration().with(configurationAspect, aspect), () -> {
            r.run();
            return null;
        });
    }

    public static <R> R with(Configuration configuration, Supplier<R> r) {
        Configuration before = CONFIGURATION.get();
        try {
            CONFIGURATION.set(configuration);
            return r.get();
        } finally {
            CONFIGURATION.set(before);
        }
    }

    public static void with(Consumer<Configuration.Builder> configuration, Runnable r) {
        Configuration.Builder builder = CONFIGURATION.get().toBuilder();
        configuration.accept(builder);
        with(builder.build(), r);
    }


    public static FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> newConfigurationMap() {
        return new FixedSizeMap<>(new HashMap<>(INITIAL_MAP));
    }

    private static FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> createConfigurationMap() {
        Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> m = new HashMap<>();
        final ServiceLoader<AlgebraicElementFormatProvider> loader =
            ServiceLoader.load(AlgebraicElementFormatProvider.class);
        loader.iterator().forEachRemaining(
            algebraicElementFormatProvider ->
                algebraicElementFormatProvider.getConfigurationAspects().forEach(c -> {
                    try {
                        ConfigurationAspect configurationAspect = c.newInstance();
                        m.put(c, configurationAspect);
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.log(Level.WARNING, e.getMessage(), e);
                    }
                }
            )
        );
        return new FixedSizeMap<>(m);
    }
}


