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
package org.meeuw.math.abstractalgebra.dim3;

import lombok.Getter;

import java.util.*;

import org.meeuw.math.abstractalgebra.*;

import static java.lang.System.Logger.Level.INFO;

/**
 * A square 3x3 matrix of any {@link ScalarFieldElement ScalarFieldElements}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix3Group<E extends ScalarFieldElement<E>>
    extends AbstractAlgebraicStructure<FieldMatrix3<E>>
    implements MultiplicativeGroup<FieldMatrix3<E>> {

    private static final System.Logger log = System.getLogger(FieldMatrix3Group.class.getName());



    public static final Map<ScalarField<?>, FieldMatrix3Group<?>> INSTANCES = new HashMap<>();

    @Getter
    private final ScalarField<E> elementStructure;

    private final FieldMatrix3<E> one;

    @SuppressWarnings("unchecked")
    public static <E extends ScalarFieldElement<E>> FieldMatrix3Group<E> of(ScalarField<E> elementStructure) {
        return (FieldMatrix3Group<E>) INSTANCES.computeIfAbsent(elementStructure, FieldMatrix3Group::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private  FieldMatrix3Group(ScalarField<E> elementStructure) {
        super((Class) FieldMatrix3.class);
        this.elementStructure = elementStructure;
        E eOne = elementStructure.one();
        E eZero = elementStructure.zero();
        one =  FieldMatrix3.of(
            elementStructure.getElementClass(),
            eOne, eZero, eZero,
            eZero, eOne, eZero,
            eZero, eZero, eOne);
        log.log(INFO, () -> "Created " + this);
    }

    @Override
    public FieldMatrix3<E> one() {
        return one;
    }

    @Override
    public FieldMatrix3<E> nextRandom(Random r) {
        return FieldMatrix3.of(
            elementStructure.getElementClass(),
            elementStructure.nextRandom(r), elementStructure.nextRandom(r), elementStructure.nextRandom(r),
            elementStructure.nextRandom(r), elementStructure.nextRandom(r), elementStructure.nextRandom(r),
            elementStructure.nextRandom(r), elementStructure.nextRandom(r), elementStructure.nextRandom(r)
        );
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.C; //elementStructure.getCardinality();
    }

}
