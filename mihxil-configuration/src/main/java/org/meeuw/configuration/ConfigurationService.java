package org.meeuw.configuration;

import lombok.extern.java.Log;

import java.util.*;
import java.util.function.*;


/**
 * A set of static methods to maintain a thread local {@link Configuration}. Every configuration has a set of {@link ConfigurationAspect} and values.
 * These values are initially loaded via java service loading. This initial default values can be changed, and also the values per thread.
 *
 * So configuration is:
 * <ol>
 * <li>Type safe. Every aspect has its own implementation with their own 'withers'</li>
 * <li>JVM global defaults can be set.</li>
 * <li>This default configuration object is the initial value of every <em>thread local</em> configuration</li>
 *</ol>
 *
 * Configuration and their aspects are unmodifiable, and can only be entirely replaced by updated values.
 *
 * @since 0.7
 */

@Log
public class ConfigurationService {

    private ConfigurationService() {
    }

    private static final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> INITIAL_MAP
        = Collections.unmodifiableMap(createInitialConfigurationMap());

    private static final Configuration.Builder DEFAULT = Configuration.builder();

    public static final ThreadLocal<Configuration> CONFIGURATION =
        ThreadLocal.withInitial(DEFAULT::build);

    /**
     * Configures the default configuration object.
     * @param  consumer the code to configure the new default configuration. it will receive a {@link Configuration.Builder} with the existing configuration.
     * @see #resetToDefaultDefaults()
     */
    public static void defaultConfiguration(Consumer<Configuration.Builder> consumer) {
        consumer.accept(DEFAULT);
        CONFIGURATION.remove();
    }

    /**
     * @return the current (thread local) configuration object
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
     * Resets all settings (via {@link #defaultConfiguration(Consumer)}.
     * @see #defaultConfiguration(Consumer)
     */

    public static void resetToDefaultDefaults() {
        defaultConfiguration(Configuration.Builder::defaults);
    }

    /**
     * @return an aspect of the current configuration object
     * @param <E> The type of the aspect to obtain
     * @param clazz the class of the aspect to obtain
     */
    public static <E extends ConfigurationAspect> E getConfigurationAspect(Class<E> clazz) {
        return CONFIGURATION.get().getAspect(clazz);
    }

     /**
     * Executes code with a certain configuration. Will set given configuration, and restore the existing
     * one after calling the supplier
     * @param configuration
     * @param
     */
    public static <R> R withConfiguration(final Configuration configuration, final Supplier<R> supplier) {
        final Configuration before = getConfiguration();
        try {
            setConfiguration(configuration);
            return supplier.get();
        } finally {
            setConfiguration(before);
        }
    }

    /**
     * Runs a piece of code with a certain {@link Configuration}
     */
    public static void withConfiguration(Configuration configuration, Runnable r) {
        withConfiguration(configuration, () -> {
            r.run();
            return null;
        });
    }


    /**
     * Runs a piece of code, but before that configure one configuration aspect
     * @param configurationAspectClass the class of the configuration aspect to configure
     * @param aspectConfigurer unary operator on a {@link ConfigurationAspect}, returns a new value for that aspect
     */
    public static <E extends ConfigurationAspect, R> R withAspect(
        Class<E> configurationAspectClass,
        UnaryOperator<E> aspectConfigurer,
        Supplier<R> r) {
        return withConfiguration(
            getConfiguration().with(configurationAspectClass, aspectConfigurer),
            r
        );
    }

    /**
     * As {@link #withAspect(Class, UnaryOperator, Supplier)}, but with a {@link Runnable} argument
     */
    public static <E extends ConfigurationAspect> void withAspect(
        Class<E> configurationAspectClass,
        UnaryOperator<E> aspectConfigurer,
        Runnable r) {
        withConfiguration(getConfiguration().with(configurationAspectClass, aspectConfigurer), () -> {
            r.run();
            return null;
        });
    }


    public static <E extends ConfigurationAspect> void withAspect(E configurationAspect, Runnable r) {
        withAspect(configurationAspect, () -> {
            r.run();
            return null;
        });
    }

    public static <E extends ConfigurationAspect, R> R withAspect(E configurationAspect, Supplier<R> r) {
        return withConfiguration(getConfiguration().toBuilder().aspectValue(configurationAspect).build(), r);
    }



    public static void withConfiguration(final Consumer<Configuration.Builder> configuration, final Runnable runnable) {
        Configuration.Builder builder = getConfiguration().toBuilder();
        configuration.accept(builder);
        withConfiguration(builder.build(), runnable);
    }


    static FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> newConfigurationMap() {
        Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> copy = createEmptyMap();
        copy.putAll(INITIAL_MAP);
        return new FixedSizeMap<>(copy);
    }

    private static FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> createInitialConfigurationMap() {
        final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> m = createEmptyMap();
        final ServiceLoader<ConfigurationAspect> loader = ServiceLoader.load(ConfigurationAspect.class);
        loader.iterator().forEachRemaining(
            configurationAspect -> {
                log.info(() -> "Found " +  configurationAspect.getClass().getCanonicalName());
                m.put(configurationAspect.getClass(), configurationAspect);
            }
        );
        return new FixedSizeMap<>(m);
    }

    private static Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> createEmptyMap() {
        return new TreeMap<>(Comparator.comparing(Class::getCanonicalName));
    }

}
