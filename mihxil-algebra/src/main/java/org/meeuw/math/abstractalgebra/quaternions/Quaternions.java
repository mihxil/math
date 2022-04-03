package org.meeuw.math.abstractalgebra.quaternions;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

/**
 * The division ring of quaternions ℍ.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public class Quaternions<E extends ScalarFieldElement<E>>
    extends AbstractAlgebraicStructure<Quaternion<E>>
    implements DivisionRing<Quaternion<E>> {

    private static final Map<ScalarField<?>, Quaternions<?>> INSTANCES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <E extends ScalarFieldElement<E>> Quaternions<E> of(ScalarField<E> numberFieldElement) {
        return (Quaternions<E>) INSTANCES.computeIfAbsent(numberFieldElement, k -> {
            Quaternions<E> result = new Quaternions<>(numberFieldElement);
            log.info("Created new instance of " + result);
            return result;

            }
        );
    }

    @Example(value = DivisionRing.class, prefix = "Quaternions ")
    public static final Quaternions<RationalNumber> H_Q = of(RationalNumbers.INSTANCE);

    @Getter
    private final ScalarField<E> elementStructure;

    private final Quaternion<E> zero;
    private final Quaternion<E> one;
    private final Quaternion<E>  i;
    private final Quaternion<E> j;
    private final Quaternion<E> k;

    private Quaternions(ScalarField<E> elementStructure) {
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

    @Override
    public String toString() {
        return "ℍ(" + elementStructure + ")";
    }
}
