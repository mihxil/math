package org.meeuw.math.text.spi;

import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.ParseException;
import java.util.*;
import java.util.function.*;
import java.util.logging.Level;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.meeuw.configuration.*;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public final class FormatService {

    private FormatService() {
    }

    private static final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> INITIAL_MAP
        = Collections.unmodifiableMap(createConfigurationMap());

    private static final Configuration.Builder DEFAULT = Configuration.builder();

    private static final ThreadLocal<Configuration> CONFIGURATION =
        ThreadLocal.withInitial(DEFAULT::build);


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

        Stream<AlgebraicElementFormatProvider> stream = StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(loader.iterator(), Spliterator.ORDERED), false);
        return stream;
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
     * Configures the default configuration object.
     * @param  consumer the code to configure the new default configuration. it will receive a {@link Configuration.Builder} with the existing configuration.
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
     * Sets the given configuration object as a thread local
     * @param configuration the new configuration
     */
    public  static void  setConfiguration(Configuration configuration) {
        CONFIGURATION.set(configuration);
    }

    /**
     * Unsets the configuration thread local, effectively resetting it the default settings.
     */

    public static void resetToDefaults() {
        CONFIGURATION.remove();
    }

    /**
     * @return an aspect of the the current configuration object
     * @param <E> The type of the aspect to obtain
     * @param clazz the class of the aspect to obtain
     */
    public static <E extends ConfigurationAspect> E getConfigurationAspect(Class<E> clazz) {
        return CONFIGURATION.get().getAspect(clazz);
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

    /**
     * Executes code with a certain configuration
     */
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
        final ServiceLoader<AlgebraicElementFormatProvider> loader = ServiceLoader.load(AlgebraicElementFormatProvider.class);
        loader.iterator().forEachRemaining(
            algebraicElementFormatProvider ->
                algebraicElementFormatProvider.getConfigurationAspects().forEach(c -> {
                    try {
                        ConfigurationAspect configurationAspect =  c.getDeclaredConstructor().newInstance();
                        m.put(c, configurationAspect);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        log.log(Level.WARNING, e.getMessage(), e);
                    }
                }
            )
        );
        return new FixedSizeMap<>(m);
    }
}


