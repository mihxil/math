package org.meeuw.physics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Unit {

    Dimensions getDimensions();

    String getDescription();

    String name();


    static UnitExponent[] toArray(Unit... units) {
        Map<Unit, AtomicInteger> map = new HashMap<>();
        for (Unit unit : units) {
            map.computeIfAbsent(unit, (u) -> new AtomicInteger(0)).incrementAndGet();
        }
        return map.entrySet()
            .stream()
            .map(e -> new UnitExponent(e.getKey(), e.getValue().intValue()))
            .toArray(UnitExponent[]::new);
    }

}
