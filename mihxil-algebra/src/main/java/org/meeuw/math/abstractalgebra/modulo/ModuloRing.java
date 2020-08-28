package org.meeuw.math.abstractalgebra.modulo;

import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * Implementation of ℤ/nℤ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class ModuloRing implements Ring<ModuloRingElement>, Streamable<ModuloRingElement> {

    final int divisor;

    private static final Map<Integer, ModuloRing> instances = new ConcurrentHashMap<>();

    public static ModuloRing of(int divisor) {
        return instances.computeIfAbsent(divisor, ModuloRing::new);
    }

    private ModuloRing(int divisor) {
        this.divisor = divisor;
    }

    @Override
    public ModuloRingElement one() {
        return new ModuloRingElement(1, this);
    }

    @Override
    public ModuloRingElement zero() {
        return new ModuloRingElement(0, this);
    }

    @Override
    public Cardinality getCardinality() {
        return new Cardinality(divisor);
    }

    @Override
    public Class<ModuloRingElement> getElementClass() {
        return ModuloRingElement.class;
    }

    @Override
    public Stream<ModuloRingElement> stream() {
        return IntStream.range(0, divisor).mapToObj(i -> new ModuloRingElement(i, this));
    }
}
