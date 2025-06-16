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
package org.meeuw.math.abstractalgebra.dim2;

import java.util.*;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.Equivalence;
import org.meeuw.math.abstractalgebra.*;

/**
 * SO(2) group.
 *
 * @author Michiel Meeuwissen
 * @since 0.16
 */
public class Rotation2Group<E extends CompleteScalarFieldElement<E>> extends AbstractAlgebraicStructure<Rotation2<E>>
    implements MultiplicativeAbelianGroup<Rotation2<E>> {

    public static final Map<CompleteScalarField<?>, Rotation2Group<?>> INSTANCES = new HashMap<>();


    private final CompleteScalarField<E> field;

    private Rotation2Group(CompleteScalarField<E> field) {
        this.field = field;
    }

    @SuppressWarnings("unchecked")
    public static <E extends CompleteScalarFieldElement<E>> Rotation2Group<E> of(CompleteScalarField<E> field) {
        return (Rotation2Group<E>) INSTANCES.computeIfAbsent(field, Rotation2Group::new);
    }

    @Override
    public Rotation2<E> one() {
        return new Rotation2<>(
            FieldMatrix2.of(
                field.one(), field.zero(),
                field.zero(), field.one()
            ), this);
    }


    public Rotation2<E> rotation(@radians E angle) {
        E sin = angle.sin();
        E cos = angle.cos();
        return new Rotation2<>(
            FieldMatrix2.of(
                cos, sin.negation(),
                sin, cos
            ), this);
    }

    public static <E extends CompleteScalarFieldElement<E>> Rotation2<E> rotationVector(@radians E angle) {
        return Rotation2Group.<E>of(angle.getStructure()).rotation(angle);
    }

   /* @Override
    public  Rotation<E> nextRandom(Random r) {
        return new Rotation<E>(field.nextRandom(r).times(2).times(field.pi()));
    }*/

    @Override
    public Cardinality getCardinality() {
        return field.getCardinality();
    }

    @Override
    public Equivalence<Rotation2<E>> getEquivalence() {
        return (e1, e2) -> e1.rot.getStructure().getEquivalence().test(e1.rot, e2.rot);
    }

    @Override
    public String toString() {
        return "SO(2)";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Rotation2Group<?> that = (Rotation2Group<?>) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(field);
    }
}
