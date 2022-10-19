/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.configuration;

import lombok.extern.java.Log;

import java.util.*;
import java.util.function.*;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;


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

    static {
        readDefaults();
        ConfigurationPreferences.addPreferenceChangeListener(DEFAULT);
        storeDefaults();
    }

    public static final ThreadLocal<Configuration> CONFIGURATION =
        ThreadLocal.withInitial(DEFAULT::build);

    /**
     * Configures the default configuration object.
     * @param  consumer the code to configure the new default configuration. it will receive a {@link Configuration.Builder} with the existing configuration.
     * @see #resetToDefaultDefaults()
     */
    public static void defaultConfiguration(Consumer<Configuration.Builder> consumer) {
        consumer.accept(DEFAULT);
        storeDefaults();
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

    public static boolean sync() {
        try {
            ConfigurationPreferences.sync();
            return true;
        } catch (BackingStoreException bs) {
            log.warning(bs.getClass().getName() + ": " + bs.getMessage());
            return false;
        }
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
     * @param configuration The configuration to run in
     * @param supplier The code the execute
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
     *
     * This is just {@link #withConfiguration(Configuration, Supplier)}, but accepting a {@link Runnable}
     */
    public static void withConfiguration(Configuration configuration, Runnable r) {
        withConfiguration(configuration,
            supplier(r)
        );
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
        withAspect(configurationAspectClass, aspectConfigurer,
            supplier(r)
        );
    }

    public static <E extends ConfigurationAspect> void withAspect(E configurationAspect, Runnable r) {
        withAspect(configurationAspect, supplier(r));
    }

    public static <E extends ConfigurationAspect, R> R withAspect(E configurationAspect, Supplier<R> r) {
        return withConfiguration(
            getConfiguration().toBuilder().aspectValue(configurationAspect).build(),
            r
        );
    }

    public static void withConfiguration(final Consumer<Configuration.Builder> configuration, final Runnable runnable) {
        Configuration.Builder builder = getConfiguration().toBuilder();
        configuration.accept(builder);
        withConfiguration(builder.build(), runnable);
    }

    /**
     * Explicitly stores the current default configuration into preferences
     */
    static void storeDefaults() {
        ConfigurationPreferences.storeDefaults(DEFAULT.build());
    }

    /**
     * Explicitly reads the current default configuration from preferences
     */
    static  void readDefaults() {
        ConfigurationPreferences.readDefaults(DEFAULT);
    }



    static FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> newConfigurationMap() {
        Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> copy = createEmptyMap();
        copy.putAll(INITIAL_MAP);
        return new FixedSizeMap<>(copy);
    }

    private static FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> createInitialConfigurationMap() {
        final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> m = createEmptyMap();
        final ServiceLoader<ConfigurationAspect> loader = ServiceLoader.load(ConfigurationAspect.class);

        final Iterator<ConfigurationAspect> iterator = loader.iterator();
        while(true) {
            try {
                if (!iterator.hasNext()) {
                    break;
                }
                ConfigurationAspect configurationAspect = iterator.next();
                log.fine(() -> "Found " + configurationAspect.getClass().getCanonicalName());
                m.put(configurationAspect.getClass(), configurationAspect);
            } catch (ServiceConfigurationError se) {
                log.warning(se.getMessage());
            } catch (Throwable e) {
                log.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return new FixedSizeMap<>(m);
    }

    private static Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> createEmptyMap() {
        return new TreeMap<>(Comparator.comparing(Class::getCanonicalName));
    }

    private static Supplier<Void> supplier(Runnable r) {
        return () -> {
            r.run();
            return null;
        };
    }
}
