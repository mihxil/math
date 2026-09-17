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

import lombok.SneakyThrows;

import java.text.Format;
import java.text.ParsePosition;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.Configuration;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.AlgebraicStructure;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

import static java.lang.System.Logger.Level.DEBUG;
import static org.meeuw.configuration.ConfigurationService.getConfiguration;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public final class FormatService {

    private static final System.Logger log = System.getLogger(FormatService.class.getName());


    private static final ThreadLocal<AlgebraicStructure<?>> CURRENT_STRUCTURE = ThreadLocal.withInitial(() -> null);

    /**
     * Per element class the applicable providers, in the order they should be tried. Resolving this
     * involves a {@link ServiceLoader} lookup plus a sort, which is far too expensive to redo on every
     * {@link #toString(AlgebraicElement)}.
     */
    private static final Map<Class<? extends AlgebraicElement<?>>, List<AlgebraicElementFormatProvider<?>>> PROVIDERS_BY_ELEMENT =
        new ConcurrentHashMap<>();

    /**
     * All providers found by the {@link ServiceLoader}, cached. The providers themselves are stateless;
     * they produce a new {@link Format} on every {@link AlgebraicElementFormatProvider#getInstance(Configuration)}.
     */
    private static volatile List<AlgebraicElementFormatProvider<?>> providers;

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
        return providersFor(elementClass).stream()
            .map(p -> p.getInstance(configuration));
    }

    /**
     * All {@link AlgebraicElementFormatProvider}s that are applicable for the given element class, the
     * heaviest (see {@link AlgebraicElementFormatProvider#weight(Class)}) first. The result is cached.
     * <p>
     * This only caches <em>which</em> providers apply. The actual {@link Format} instances are still
     * created per call, so they keep honouring the current {@link Configuration}.
     */
    private static List<AlgebraicElementFormatProvider<?>> providersFor(Class<? extends AlgebraicElement<?>> elementClass) {
        return PROVIDERS_BY_ELEMENT.computeIfAbsent(elementClass, c -> {
            final List<AlgebraicElementFormatProvider<?>> list = new ArrayList<>();
            getProviders().forEach(list::add);
            list.removeIf(e -> e.weight(c) < 0);
            list.sort(Comparator.comparingInt(e -> -1 * e.weight(c)));
            return List.copyOf(list);
        });
    }

    /**
     * Drops the cache filled by {@link #getFormat(Class, Configuration)}. Only needed if the available
     * {@link AlgebraicElementFormatProvider}s changed after they were first used.
     *
     * @since 0.21
     */
    public static void clearCache() {
        PROVIDERS_BY_ELEMENT.clear();
        providers = null;
    }

    @SuppressWarnings("unchecked")
    public static <P extends AlgebraicElementFormatProvider<F>, F extends Format> F getFormat(Class<P> clazz) {
        return (F) getProviders().filter(clazz::isInstance).findFirst().map(p -> p.getInstance(ConfigurationService.getConfiguration())).orElse(null);
    }

    public static Stream<AlgebraicElementFormatProvider<?>> getProviders() {
        return getProviderList().stream();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<AlgebraicElementFormatProvider<?>> getProviderList() {
        List<AlgebraicElementFormatProvider<?>> result = providers;
        if (result == null) {
            final ServiceLoader<AlgebraicElementFormatProvider<?>> loader = (ServiceLoader)
                ServiceLoader.load(AlgebraicElementFormatProvider.class);
            result = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(loader.iterator(), Spliterator.ORDERED), false)
                .toList();
            providers = result;
        }
        return result;
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
                log.log(DEBUG, () -> "" + f);
                try {
                    return f.format(object);
                } catch (IllegalArgumentException iea) {
                    log.log(DEBUG,iea.getMessage());
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

    @SneakyThrows
    public static <E extends AlgebraicElement<E>> E fromString(AlgebraicStructure<E> structure, final String source, Class<E> clazz) throws NotParsable {
        return with(structure, () -> fromString(source, clazz, getConfiguration()));
    }



    @SuppressWarnings("unchecked")
    public static <E extends AlgebraicElement<E>> E fromString(final String source, Class<E> clazz, Configuration configuration) throws NotParsable {
        return getFormat(clazz, configuration)
            .map(f -> {
                ParsePosition pos = new ParsePosition(0);
                E parsed =  (E) f.parseObject(source, pos);
                if (pos.getErrorIndex() > 0 || pos.getIndex() != source.length()) {
                    log.log(DEBUG,() -> "Could not parse '" + source + "' with " + f);
                    return null;
                }
                return parsed;
            })
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> {
                List<Format> list = getFormat(clazz, configuration).toList();
                return new NotParsable.NotImplemented("Could not parse to " + clazz
                    +  (!list.isEmpty() ? " (with  " + list + ")" : ""), source);
                }
            );
    }


    public static <E extends AlgebraicElement<E>, R> R with(AlgebraicStructure<E> structure, Callable<R> r) throws Exception {
        try {
            CURRENT_STRUCTURE.set(structure);
            return r.call();
        } finally {
            CURRENT_STRUCTURE.remove();
        }

    }

    @SuppressWarnings("unchecked")
    public static <E extends AlgebraicElement<E>, S extends AlgebraicStructure<E>> S getCurrentStructure() {
        return (S) CURRENT_STRUCTURE.get();
    }


}


