package org.meeuw.configuration;

import lombok.extern.java.Log;

import java.util.*;
import java.util.function.*;


/**
 * @since 0.7
 */

@Log
public class ConfigurationService {

    private ConfigurationService() {
    }


    private static final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> INITIAL_MAP
        = Collections.unmodifiableMap(createConfigurationMap());

    private static final Configuration.Builder DEFAULT = Configuration.builder();

    public static final ThreadLocal<Configuration> CONFIGURATION =
        ThreadLocal.withInitial(DEFAULT::build);


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

    public static void with(Configuration configuration, Runnable r) {
        with(configuration, () -> {
            r.run();
            return null;
        });
    }

    public static <E extends ConfigurationAspect, R> R with(Class<E> configurationAspect, UnaryOperator<E> aspect, Supplier<R> r) {
        return with(
            getConfiguration().with(configurationAspect, aspect),
            r
        );
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
        Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> copy = createEmptyMap();
        copy.putAll(INITIAL_MAP);
        return new FixedSizeMap<>(copy);
    }

    private static FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> createConfigurationMap() {
        final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> m = createEmptyMap();
        final ServiceLoader<ConfigurationAspect> loader = ServiceLoader.load(ConfigurationAspect.class);
        loader.iterator().forEachRemaining(
            configurationAspect -> {
                m.put(configurationAspect.getClass(), configurationAspect);
            }
        );
        return new FixedSizeMap<>(m);
    }

    private static Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> createEmptyMap() {
        return new TreeMap<>(Comparator.comparing(Class::getSimpleName));
    }

}
