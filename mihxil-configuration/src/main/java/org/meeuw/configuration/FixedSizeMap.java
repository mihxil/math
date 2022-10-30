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
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FixedSizeMap<K, V> extends AbstractMap<K, V> {

    private final Map<K, V> wrapped;

    public FixedSizeMap(Map<K, V> wrapped) {
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
    public V get(Object k) {
        return wrapped.get(k);
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
