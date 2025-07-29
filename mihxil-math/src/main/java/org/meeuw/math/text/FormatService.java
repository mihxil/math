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
package org.meeuw.math.text;

import lombok.Generated;
import lombok.extern.java.Log;

import java.text.Format;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.*;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

import static org.meeuw.configuration.ConfigurationService.getConfiguration;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public final class FormatService {

    private FormatService() {
    }


    /**
     * @param object an algebraic element for which a {@code Format} is needed
     * @param configuration an object to configure these instances
     * @return all available {@link Format} instances that would be available for the given algebraic element
     */
    @SuppressWarnings("unchecked")
    public static Stream<Format> getFormat(AlgebraicElement<?> object, Configuration configuration) {
        return getFormat((Class<? extends AlgebraicElement<?>>) object.getClass(), configuration);
    }

    public static Stream<Format> getFormat(Class<? extends AlgebraicElement<?>> elementClass, Configuration configuration) {
        final List<AlgebraicElementFormatProvider<?>> list = new ArrayList<>();
        getProviders().forEach(list::add);
        list.removeIf(e -> e.weight(elementClass) < 0);
        list.sort(Comparator.comparingInt(e -> -1 * e.weight(elementClass)));
        return list.stream().map(p -> p.getInstance(configuration));
    }

    @SuppressWarnings("unchecked")
    public static <P extends AlgebraicElementFormatProvider<F>, F extends Format> F getFormat(Class<P> clazz) {
        return (F) getProviders().filter(clazz::isInstance).findFirst().map(p -> p.getInstance(ConfigurationService.getConfiguration())).orElse(null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Stream<AlgebraicElementFormatProvider<?>> getProviders() {
        final ServiceLoader<AlgebraicElementFormatProvider<?>> loader = (ServiceLoader)
            ServiceLoader.load(AlgebraicElementFormatProvider.class);

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(loader.iterator(), Spliterator.ORDERED), false);
    }

    /**
     * @param object the element to creaate an string representation for
     * @return string representation of the given algebraic element.
     */
    public static String toString(AlgebraicElement<?> object) {
        return toString(object, getConfiguration());
    }

    public static String toString(@NonNull AlgebraicElement<?> object, Configuration configuration) {
        return getFormat(object, configuration)
            .map(f -> {
                log.fine(() -> "" + f);
                try {
                    return f.format(object);
                } catch (IllegalArgumentException iea) {
                    log.fine(iea.getMessage());
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("<TO STRING " + object.getClass().getName() + " FAILED>");
    }

    public static <E extends AlgebraicElement<E>> E fromString(final String source, Class<E> clazz) throws NotParsable {
        return fromString(source, clazz, getConfiguration());
    }


    @SuppressWarnings("unchecked")
    public static <E extends AlgebraicElement<E>> E fromString(final String source, Class<E> clazz, Configuration configuration) throws NotParsable {
        return getFormat(clazz, configuration)
            .map(f -> {
                try {
                    E parsed =  (E) f.parseObject(source);
                    return parsed;
                } catch (ParseException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new NotParsable.NotImplemented("Could not parse '" + source + "' to " + clazz + " (with " + getFormat(clazz, configuration).toList() + ")"));
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Generated // exclude from coverage
    public static void setConfiguration(Configuration build) {
        ConfigurationService.setConfiguration(build);
    }
    @Deprecated
    @Generated
    public static <E extends ConfigurationAspect> void with(Class<E> configurationAspect, UnaryOperator<E> aspect, Runnable r) {
        ConfigurationService.withAspect(configurationAspect, aspect, r);
    }

    @Deprecated
    @Generated
    public static void with(Consumer<Configuration.Builder> configuration, Runnable r) {
        ConfigurationService.withConfiguration(configuration, r);
    }


}


