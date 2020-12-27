package org.meeuw.math.abstractalgebra.integers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of ℤ/nℤ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloRing extends ModuloStructure<ModuloRingElement, ModuloRing> {

    private static final Map<Integer, ModuloRing> instances = new ConcurrentHashMap<>();

    public static ModuloRing of(int divisor) {
        return instances.computeIfAbsent(divisor, ModuloRing::new);
    }

    private ModuloRing(int divisor) {
        super(ModuloRingElement.class, divisor);
    }

    @Override
    ModuloRingElement element(int v) {
        return new ModuloRingElement(v, this);
    }

}
