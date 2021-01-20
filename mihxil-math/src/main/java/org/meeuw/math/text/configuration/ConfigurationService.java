package org.meeuw.math.text.configuration;

import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.function.*;

import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class ConfigurationService {

    private static final ConfigurationService.Builder DEFAULT = ConfigurationService.builder();

    private static final ThreadLocal<ConfigurationService> CONFIGURATION =
        ThreadLocal.withInitial(DEFAULT::build);


    public static void configureDefault(Consumer<ConfigurationService.Builder> consumer) {
        consumer.accept(DEFAULT);
        CONFIGURATION.remove();
    }

    public static ConfigurationService get() {
        return CONFIGURATION.get();
    }

    public static <E extends Configuration> E getConfiguration(Class<E> clazz) {
        return CONFIGURATION.get().get(clazz);
    }

    public static void with(ConfigurationService configuration, Runnable r) {
        with(configuration, () -> {r.run(); return null;});
    }

    public static <R> R with(ConfigurationService configuration, Supplier<R> r) {
        try {
            CONFIGURATION.set(configuration);
            return  r.get();
        } finally {
            CONFIGURATION.remove();
        }
    }

    public static void with(Consumer<Builder> configuration, Runnable r) {
        Builder builder = CONFIGURATION.get().toBuilder();
        configuration.accept(builder);
        with(builder.build(), r);
    }

    private final Map<Class<? extends Configuration>, Configuration> map;

    static FixedSizeMap<Class<? extends Configuration>, Configuration> createConfigurationMap() {
         Map<Class<? extends Configuration>, Configuration> m  = new HashMap<>();
         final ServiceLoader<AlgebraicElementFormatProvider> loader =
             ServiceLoader.load(AlgebraicElementFormatProvider.class);
         loader.iterator().forEachRemaining(
             algebraicElementFormatProvider ->
                 algebraicElementFormatProvider.getConfigurationSettings().forEach(c ->
                     m.put(c.getClass(), c)
                 )
         );
         return new FixedSizeMap<>(m);
     }

    static Map<Class<? extends Configuration>, Configuration> unmodifiableCopy(Map<Class<? extends Configuration>, Configuration> configuration) {
        FixedSizeMap<Class<? extends Configuration>, Configuration> newMap = createConfigurationMap();
        newMap.putAll(configuration);
        return Collections.unmodifiableMap(newMap);

    }


    public ConfigurationService(Map<Class<? extends Configuration>, Configuration> configuration) {
        this.map = unmodifiableCopy(configuration);
    }


    @SuppressWarnings("unchecked")
    public <E extends Configuration> E get(Class<E> clazz) {
        return (E) map.get(clazz);
    }

    public <E extends  Configuration> ConfigurationService with(Class<E> clazz, UnaryOperator<E> config) {
        return toBuilder().config(clazz, config).build();
    }

    public <E extends Configuration, R> R with(Class<E> clazz, UnaryOperator<E> config, Supplier<R> function) {
        return with(with(clazz, config), function);
    }

    public void run(Runnable run) {
        with(this, run);
    }

    public Builder toBuilder() {
        FixedSizeMap<Class<? extends Configuration>, Configuration> newMap = createConfigurationMap();
        newMap.putAll(map);
        return new Builder(newMap);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FixedSizeMap<Class<? extends Configuration>, Configuration> configuration;

        public Builder(FixedSizeMap<Class<? extends Configuration>, Configuration> configuration) {
            this.configuration = configuration;
        }
        public Builder() {
            this.configuration = createConfigurationMap();
        }

        @SuppressWarnings("unchecked")
        public <E extends Configuration> Builder config(Class<E> clazz, UnaryOperator<E> config) {
            E template = (E) configuration.get(clazz);
            E newConfig = config.apply(template);
            configuration.put(clazz, newConfig);
            return this;
        }

        public ConfigurationService build() {
            return new ConfigurationService(configuration);
        }
    }

    static class FixedSizeMap<K, V> extends AbstractMap<K, V> {

        private final Map<K, V> wrapped;

        private FixedSizeMap(Map<K, V> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public V put(K k, V v) {
            if (wrapped.containsKey(k)) {
                return wrapped.put(k, v);
            } else {
                throw new UnsupportedOperationException();
            }
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            return new AbstractSet<Entry<K, V>>() {
                @Override
                public Iterator<Entry<K, V>> iterator() {
                    return wrapped.entrySet().iterator();
                }

                @Override
                public int size() {
                    return wrapped.size();
                }
            };

        }
    }

}
