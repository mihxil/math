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

import lombok.Getter;

import java.util.*;

import org.meeuw.math.abstractalgebra.*;

/**
 * A square 3x3 matrix of any {@link ScalarFieldElement ScalarFieldElements}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix2Group<E extends ScalarFieldElement<E>>
    extends AbstractAlgebraicStructure<FieldMatrix2<E>>
    implements MultiplicativeGroup<FieldMatrix2<E>> {


    public static final Map<ScalarField<?>, FieldMatrix2Group<?>> INSTANCES = new HashMap<>();

    @Getter
    private final ScalarField<E> elementStructure;

    private final FieldMatrix2<E> one;

    @SuppressWarnings("unchecked")
    public static <E extends ScalarFieldElement<E>> FieldMatrix2Group<E> of(ScalarField<E> elementStructure) {
        return (FieldMatrix2Group<E>) INSTANCES.computeIfAbsent(elementStructure, FieldMatrix2Group::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private FieldMatrix2Group(ScalarField<E> elementStructure) {
        super((Class) FieldMatrix2.class);
        this.elementStructure = elementStructure;
        E eOne = elementStructure.one();
        E eZero = elementStructure.zero();
        one =  FieldMatrix2.of(
            elementStructure.getElementClass(),
            eOne, eZero,
            eZero, eOne);
    }

    @Override
    public FieldMatrix2<E> one() {
        return one;
    }


    @Override
    public FieldMatrix2<E> nextRandom(Random r) {
        return FieldMatrix2.of(
            elementStructure.getElementClass(),
            elementStructure.nextRandom(r), elementStructure.nextRandom(r),
            elementStructure.nextRandom(r), elementStructure.nextRandom(r)
        );
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.C; //elementStructure.getCardinality();
    }

}
