package org.meeuw.configuration;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static org.meeuw.configuration.ConfigurationService.newConfigurationMap;

/**
 * Immutable object containing all {@link ConfigurationAspect}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class Configuration {

    private final Map<Class<? extends ConfigurationAspect>, ConfigurationAspect> map;

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

    public <E extends ConfigurationAspect> Configuration with(E aspect) {
        return toBuilder().aspectValue(aspect).build();
    }

    public List<ConfigurationAspect> getConfigurationAspectsAssociatedWith(Class<?> clazz) {
        return map.values().stream()
            .filter(aspect -> aspect.associatedWith().stream().anyMatch(clazz::isAssignableFrom))
            .collect(Collectors.toList());
    }


    public Builder toBuilder() {
        FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> newMap = newConfigurationMap();
        newMap.putAll(map);
        return new Builder(newMap);
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {
        private final FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration;

        public Builder(FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration) {
            this.configuration = configuration;
        }
        public Builder() {
            this.configuration = newConfigurationMap();
        }

        @SuppressWarnings("unchecked")
        public <E extends ConfigurationAspect> Builder configure(Class<E> clazz, UnaryOperator<E> config) {
            E template = (E) configuration.get(clazz);
            E newConfig = config.apply(template);
            configuration.put(clazz, newConfig);
            return this;
        }

        public <E extends ConfigurationAspect> Builder aspectValue(E value) {
            configuration.put(value.getClass(), value);
            return this;
        }

        @SneakyThrows
        public <E extends ConfigurationAspect> Builder aspectDefault(Class<E> clazz) {
            configuration.put(clazz, clazz.newInstance());
            return this;
        }


        public Builder defaults() {
            for (Class<? extends ConfigurationAspect> c : configuration.keySet()) {
                aspectDefault(c);
            }
            return this;
        }

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
