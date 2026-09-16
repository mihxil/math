/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.configuration;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static org.meeuw.configuration.ConfigurationService.newConfigurationMap;

/**
 * Immutable object containing all {@link ConfigurationAspect}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class Configuration implements Iterable<ConfigurationAspect> {

    final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> map;

    private Configuration(Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration) {
        this.map = immutableCopy(configuration);
    }

    /**
     * Returns the aspect with given class.
     * @param <E> the type of the configuration aspect
     * @param clazz the class of the configuration aspect
     * @return the configuration aspect instance of that class currently configured in this configuration object
     */
    @SuppressWarnings("unchecked")
    public <E extends ConfigurationAspect> E getAspect(Class<E> clazz) {
        E result =  (E) map.get(clazz);
        if (result == null) {
            throw new ConfigurationException("No configuration aspect with class " + clazz + " registered");
        }
        return result;
    }

    /**
     * Gets a value of a certain configuration aspect
     * @param <E> the type of the configuration aspect
     * @param <V> the type of the configuration aspect value
     * @param clazz the class of the configuration aspect
     * @param getter a function to get the value from the instance of the aspect (probably a method reference)
     * @return the request configuration aspect value
     */
    public <E extends ConfigurationAspect, V> V getAspectValue(Class<E> clazz, Function<E, V> getter) {
        return getter.apply(getAspect(clazz));
    }

    /**
     * @return a new {@link Configuration} with a changed {@link ConfigurationAspect}
     * @param clazz The configuration aspect class
     * @param <E> the type of the class
     * @param config The operator that given the exising value for the aspect, produces a new one
     */
    public <E extends ConfigurationAspect> Configuration with(Class<E> clazz, UnaryOperator<E> config) {
        return toBuilder()
            .configure(clazz, config
        ).build();
    }

    /**
     * Returns the configuration aspects associated with the given type.
     *
     * @param clazz the type for which to find associated aspects
     * @return the associated configuration aspects
     */
    public List<ConfigurationAspect> getConfigurationAspectsAssociatedWith(Class<?> clazz) {
        return map.values().stream()
            .filter(aspect -> aspect.associatedWith().stream().anyMatch(clazz::isAssignableFrom))
            .collect(Collectors.toList());
    }


    /**
     * Converts this configuration into a new {@link Builder}, so it can be converted to a new configuration set.
     */
    public Builder toBuilder() {
        FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> newMap = newConfigurationMap();
        newMap.putAll(map);
        return new Builder(newMap);
    }


    /**
     * @return returns a new {@link Builder} to assemble a new configuration
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Iterator<ConfigurationAspect> iterator() {
        return map.values().iterator();
    }


    @Override
    public String toString() {
        return Configuration.class.getSimpleName() + ":" + map.toString();
    }
    /**
     * Builder pattern for {@link Configuration}.
     */
    @SuppressWarnings("UnusedReturnValue")
    @Log
    public static class Builder {
        private final FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration;

        /**
         * Creates a builder backed by the given configuration map.
         *
         * @param configuration the configuration aspects to initialize the builder with
         */
        public Builder(FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration) {
            this.configuration = configuration;
        }

        Builder() {
            this.configuration = newConfigurationMap();
        }

        /**
         * Configures one certain aspect of configuration.
         * @param aspect The aspect to configure
         * @param configOperator The code to change it
         */
        @SuppressWarnings("unchecked")
        public <E extends ConfigurationAspect> Builder configure(Class<E> aspect, UnaryOperator<E> configOperator) {
            E template = (E) configuration.get(aspect);
            E newConfig = configOperator.apply(template);
            configuration.put(aspect, newConfig);
            return this;
        }

        /**
         * Configures an aspect by invoking one of its single-argument wither methods. This makes it possible
         * to (optionally) configure an aspect without the dependency. Used in {code org.meeuw.jupiter.SetNumberConfiguration}
         * to configure aspects which may be available, but not necessarily.
         *
         * @param aspectClass the fully qualified name of the configuration aspect class
         * @param method the wither method to invoke
         * @param value the value to pass to the method
         * @return this builder
         */
        public Builder configure(String aspectClass, String method, Object value) {
            try {
                Class<ConfigurationAspect> configurationAspectClass = (Class<ConfigurationAspect>) Class.forName(aspectClass);
                var unary = new UnaryOperator<ConfigurationAspect>() {
                    @Override
                    public ConfigurationAspect apply(ConfigurationAspect configurationAspect) {
                        Method[] methods = configurationAspectClass.getDeclaredMethods();
                        ConfigurationAspect e = configurationAspect;
                        Object effectiveValue = value;
                        for (Method m : methods) {
                            if (m.getName().equals(method) && m.getParameterCount() == 1 && m.getReturnType().equals(e.getClass())) {
                                try {
                                    if (Enum.class.isAssignableFrom(m.getParameterTypes()[0]) && value instanceof CharSequence) {
                                        effectiveValue = Enum.valueOf((Class<Enum>)m.getParameterTypes()[0], value.toString());
                                    }
                                    e = (ConfigurationAspect) m.invoke(configurationAspect, effectiveValue);
                                    break;
                                } catch (IllegalAccessException | InvocationTargetException ex) {
                                    log.log(Level.WARNING, ex.getMessage(), ex);
                                }
                            }
                        }
                        return e;

                    }
                };
                configure(configurationAspectClass, unary);
            } catch (ClassNotFoundException e) {
                log.log(Level.FINE, e.getMessage());

            }
            return this;
        }

        /**
         * Replaces a configuration aspect with the given value.
         *
         * @param <E> the type of the configuration aspect
         * @param value the replacement aspect value
         * @return this builder
         */
        public <E extends ConfigurationAspect> Builder aspectValue(E value) {
            configuration.put(value.getClass(), value);
            return this;
        }

        /**
         * Replaces a configuration aspect with a newly constructed default instance.
         *
         * @param <E> the type of the configuration aspect
         * @param clazz the class of the aspect to reset
         * @return this builder
         */
        @SneakyThrows
        public <E extends ConfigurationAspect> Builder aspectDefault(Class<E> clazz) {
            configuration.put(clazz, clazz.getDeclaredConstructor().newInstance());
            return this;
        }


        /**
         * Resets every configured aspect to its default instance.
         *
         * @return this builder
         */
        public Builder defaults() {
            for (Class<? extends ConfigurationAspect> c : configuration.keySet()) {
                aspectDefault(c);
            }
            return this;
        }

        /**
         * Creates an immutable configuration from the currently configured aspects.
         *
         * @return the assembled configuration
         */
        public Configuration build() {
            return new Configuration(configuration);
        }
    }

    private static Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> immutableCopy(Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration) {
        FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> newMap = newConfigurationMap();
        newMap.putAll(configuration);
        return Collections.unmodifiableMap(newMap);

    }
}
