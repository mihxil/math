package org.meeuw.physics;

import lombok.NonNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Unit extends Units {

    /**
     * @return the dimensional analysis for this unit
     */
    @Override
    Dimensions getDimensions();

    String getDescription();

    String name();


    @Override
    @NonNull
    default Iterator<UnitExponent> iterator() {
        return Collections.singleton(new UnitExponent(this, 1)).iterator();
    }

    static UnitExponent[] toArray(Unit... units) {
        Map<Unit, AtomicInteger> map = new LinkedHashMap<>();
        for (Unit unit : units) {
            map.computeIfAbsent(unit, (u) -> new AtomicInteger(0)).incrementAndGet();
        }
        return map.entrySet()
            .stream()
            .map(e -> new UnitExponent(e.getKey(), e.getValue().intValue()))
            .toArray(UnitExponent[]::new);
    }

}
