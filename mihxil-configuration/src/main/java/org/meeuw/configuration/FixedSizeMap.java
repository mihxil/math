package org.meeuw.configuration;

import java.util.*;

/**
 * A simple {@link Map} implementation which allows for changing mappings, but not adding or deleting them.
 *
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
