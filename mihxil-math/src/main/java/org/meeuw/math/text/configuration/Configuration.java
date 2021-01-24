package org.meeuw.math.text.configuration;

import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.meeuw.math.text.FixedSizeMap;

import static org.meeuw.math.text.spi.FormatServiceProvider.newConfigurationMap;

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
     */
    @SuppressWarnings("unchecked")
    public <E extends ConfigurationAspect> E get(Class<E> clazz) {
        E result =  (E) map.get(clazz);
        if (result == null) {
            throw new ConfigurationException("No configuration aspect with class " + clazz + " registered");
        }
        return result;
    }

    /**
     * Returns a new {@link Configuration} with a changed {@link ConfigurationAspect}
     * @param clazz The configuration aspect class
     * @param config The operator that given the exising value for the aspect, produces a new one
     */
    public <E extends ConfigurationAspect> Configuration with(Class<E> clazz, UnaryOperator<E> config) {
        return toBuilder().config(clazz, config).build();
    }

    public Builder toBuilder() {
        FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> newMap = newConfigurationMap();
        newMap.putAll(map);
        return new Builder(newMap);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration;

        public Builder(FixedSizeMap<Class<? extends ConfigurationAspect>, ConfigurationAspect> configuration) {
            this.configuration = configuration;
        }
        public Builder() {
            this.configuration = newConfigurationMap();
        }

        @SuppressWarnings("unchecked")
        public <E extends ConfigurationAspect> Builder config(Class<E> clazz, UnaryOperator<E> config) {
            E template = (E) configuration.get(clazz);
            E newConfig = config.apply(template);
            configuration.put(clazz, newConfig);
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
