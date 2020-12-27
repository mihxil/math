package org.meeuw.math.text.spi;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class Configuration {

    public static final Configuration.Builder DEFAULT = Configuration.builder()
        .minimalExponent(4);

    private static final ThreadLocal<Configuration> CONFIGURATION = ThreadLocal.withInitial(DEFAULT::build);

    public static Configuration get() {
        return CONFIGURATION.get();
    }

    public static void with(Configuration configuration, Runnable r) {
        try {
            CONFIGURATION.set(configuration);
            r.run();
        } finally {
            CONFIGURATION.remove();
        }
    }

    public static void with(Consumer<Builder> configuration, Runnable r) {
        Builder builder = CONFIGURATION.get().toBuilder();
        configuration.accept(builder);
        with(builder.build(), r);
    }

    public enum UncertaintyNotation {
        PLUS_MINUS,
        PARENTHESES
    }

    @Getter
    private final int minimalExponent;

    @Getter
    private final UncertaintyNotation uncertaintyNotation;

    private final Map<String, Object> properties = new HashMap<>();

    @lombok.Builder(toBuilder = true, buildMethodName = "_build")
    private  Configuration(
        Integer minimalExponent,
        UncertaintyNotation uncertaintyNotation,
        Map<String, Object> properties) {
        this.minimalExponent = minimalExponent == null ? 4 : minimalExponent;
        this.uncertaintyNotation = uncertaintyNotation == null ? UncertaintyNotation.PLUS_MINUS : uncertaintyNotation;
        this.properties.putAll(properties);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPropertyOrDefault(String key, T defaultValue) {
        return (T) properties.getOrDefault(key, defaultValue);
    }

    public static class Builder {
        Map<String, Object> p = new HashMap<>();
        public Configuration.Builder property(String key, Object object) {
            p.put(key, object);
            return this;
        }
        public Configuration build() {
            return properties(p)._build();
        }
    }

}
