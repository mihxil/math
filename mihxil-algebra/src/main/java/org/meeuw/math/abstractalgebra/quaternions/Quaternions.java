package org.meeuw.math.abstractalgebra.quaternions;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public class Quaternions<E extends NumberFieldElement<E>> extends AbstractAlgebraicStructure<Quaternion<E>>
    implements DivisionRing<Quaternion<E>> {

    private static final Map<NumberField<?>, Quaternions<?>> INSTANCES = new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    public static <E extends NumberFieldElement<E>> Quaternions<E> of(NumberField<E> numberFieldElement) {
        return (Quaternions<E>) INSTANCES.computeIfAbsent(numberFieldElement, k -> {
            log.info("Created new instance for " + k);
            return new Quaternions<>(k);
            }
        );
    }

    @Getter
    private final NumberField<E> elementStructure;

    private final Quaternion<E> zero;
    private final Quaternion<E> one;
    private final Quaternion<E> i;
    private final Quaternion<E> j;
    private final Quaternion<E> k;

    private Quaternions(NumberField<E> elementStructure) {
        super((Class) Quaternion.class);
        this.elementStructure = elementStructure;
        E z = this.elementStructure.zero();
        E u = this.elementStructure.one();
        this.zero = new Quaternion<>(z, z, z, z);
        this.one  = new Quaternion<>(u, z, z, z);
        this.i    =  new Quaternion<>(z, u, z, z);
        this.j    =  new Quaternion<>(z, z, u, z);
        this.k    =  new Quaternion<>(z, z, z, u);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }
    @Override
    public Quaternion<E> zero() {
        return zero;
    }

    @Override
    public Quaternion<E> one() {
        return one;
    }

    public Quaternion<E> i() {
        return i;
    }
    public Quaternion<E> j() {
        return j;
    }
    public Quaternion<E> k() {
        return k;
    }
}
