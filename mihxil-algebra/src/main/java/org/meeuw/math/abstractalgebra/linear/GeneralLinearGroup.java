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
package org.meeuw.math.abstractalgebra.linear;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.validation.Square;

/**
 * A linear group on any {@link org.meeuw.math.abstractalgebra.Field}
 */
public class GeneralLinearGroup<E extends FieldElement<E>> extends
    AbstractGeneralLinearGroup<
        InvertibleMatrix<E>,
        GeneralLinearGroup<E>,
        E,
        Field<E>
        > {

    private static final Map<Key, GeneralLinearGroup<?>> INSTANCES = new ConcurrentHashMap<>();

    @Example(value = MultiplicativeGroup.class, prefix = "GeneralLinearGroup ")
    public static GeneralLinearGroup<RationalNumber> GL2_Q = of(2, RationalNumbers.INSTANCE);

    protected GeneralLinearGroup(@NonNull Field<E> elementStructure, int dimension) {
        super(elementStructure, dimension);
    }

    @SuppressWarnings("unchecked")
    public static <E extends FieldElement<E>> GeneralLinearGroup<E> of(final int length, final Field<E> elementStructure) {
        final Key key = new Key(length, elementStructure);
        return (GeneralLinearGroup<E>) INSTANCES.computeIfAbsent(key, k -> new GeneralLinearGroup<E>(
            elementStructure,
            k.dimension
        ));
    }

    @Override
    InvertibleMatrix<E> of(@Square(invertible = true) E[][] elements) {
        return new InvertibleMatrix<>(this, elements);
    }



}
