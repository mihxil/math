package org.meeuw.math.text;

import java.util.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
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
