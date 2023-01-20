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

import java.util.*;

/**
 * A simple {@link Map} implementation which allows for changing mappings, but not adding or deleting them.
 * <p>
 * This is used when representing configurations.
 * <p>
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FixedSizeMap<K, V> extends AbstractMap<K, V> {

    private final Map<K, V> wrapped;

    /**
     * Instantiate this map with a given map of keys and values.
     * This will be wrapped, and {@code FixedSizeMap} will disallow putting keys which are not already in it (or removing keys from it)
     */
    public FixedSizeMap(Map<K, V> wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Instantiate this map with the needed keys. All values will initially be {@code null}
     */
    @SafeVarargs
    public FixedSizeMap(K... keys) {
        this.wrapped = new LinkedHashMap<>();
        for (K key : keys) {
            wrapped.put(key, null);
        }
    }

    /**
     * Creates a {@link FixedSizeMap} and intializes all values.
     *
     * @param keysAndValues And even list of objects. Even entries are keys, odd entries are values.
     * @throws ClassCastException If one of the objects it not of the correct type
     * @throws IndexOutOfBoundsException If the number of parameters is not even.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> FixedSizeMap<K, V> of(Object ... keysAndValues) {
        Map<K, V> wrapped = new LinkedHashMap<>();
        Class<K> keyType = null;
        Class<V> valueType = null;
        for (int i = 0; i < keysAndValues.length; i += 2) {
            K key = (K) keysAndValues[i];
            if (key == null) {
                throw new IllegalArgumentException("keys cannot be null");
            }
            if (keyType == null) {
                keyType = (Class<K>) key.getClass();
            } else {
                if (! keyType.isAssignableFrom(key.getClass())) {
                    throw new ClassCastException("Key " + key + " has unexpected type (not assignable to " + keyType + ")");
                }
            }
            V value = (V) keysAndValues[i + 1];
            if (value != null) {
                if (valueType == null) {
                    valueType = (Class<V>) value.getClass();
                } else {
                    if (! valueType.isAssignableFrom(value.getClass())) {
                        throw new ClassCastException("Value " + value + " has unexpected type (not assignable to " + valueType + ")");
                    }
                }
            }
            wrapped.put(key, value);
        }

        return new FixedSizeMap<K, V>(wrapped);
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
    public V get(Object k) {
        return wrapped.get(k);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                Iterator<Entry<K, V>> wrappedIterator = wrapped.entrySet().iterator();
                // makes unmodifiable.
                return new Iterator<>() {

                    @Override
                    public boolean hasNext() {
                        return wrappedIterator.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        return wrappedIterator.next();
                    }
                };

            }

            @Override
            public int size() {
                return wrapped.size();
            }
        };

    }
}
