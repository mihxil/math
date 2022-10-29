package org.meeuw.configuration;

import java.util.*;
import java.util.stream.Stream;

import org.meeuw.configuration.spi.ToStringProvider;

public class StringConversionService {

    public static <C> Optional<String> toString(C value) {
        return  stream()
            .sorted()
            .map(tp ->
                tp.toString(value).orElse(null)
            )
            .filter(Objects::nonNull)
            .findFirst();
    }
    public static <C> Optional<C> fromString(String value, Class<? extends C> type) {
        return  (Optional<C>) StringConversionService.stream()
            .sorted()
            .map(tp -> {
                return tp.fromString(type, value).orElse(null);
            })
            .filter(Objects::nonNull)
            .findFirst();
    }

    @SuppressWarnings("unchecked")
    static Stream<ToStringProvider<?>> stream() {
        ServiceLoader<ToStringProvider<?>> loader = (ServiceLoader) ServiceLoader.load(ToStringProvider.class);
        //return loader.stream();  java 9, damn.
        List<ToStringProvider<?>> list = new ArrayList<>();
        loader.iterator().forEachRemaining(list::add);
        return list.stream();
    }


}
