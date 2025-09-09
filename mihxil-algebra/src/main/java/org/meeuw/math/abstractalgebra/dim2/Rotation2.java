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

import java.util.function.UnaryOperator;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.validation.Square;

/**
 * @author Michiel Meeuwissen
 * @since 0.16
 */
public class Rotation2<E extends CompleteScalarFieldElement<E>> implements
    MultiplicativeGroupElement<Rotation2<E>>,
    UnaryOperator<FieldVector2<E>> {

    final FieldMatrix2<E> rot;
    final Rotation2Group<E> structure;

    private Rotation2(@Square(2) E[][] values) {
        rot = FieldMatrix2.of(
            values[0][0], values[0][1],
            values[1][0], values[1][1]
        );
        this.structure = Rotation2Group.of(values[0][0].getStructure());
    }
    Rotation2(FieldMatrix2<E> rot, Rotation2Group<E> rotGroup) {
        this.rot = rot;
        this.structure = rotGroup;
    }




    @Override
    public @NonNull Rotation2Group<E> getStructure() {
        return structure;
    }

    @Override
    public Rotation2<E> times(Rotation2<E> multiplier) {
        return new Rotation2<E>(
            rot.times(multiplier.rot), structure
        );
    }

    protected FieldVector2<E> rotate(FieldVector2<E> in) {
        return in.times(rot);
    }

    public FieldMatrix2<E> asMatrix() {
        return rot;
    }

    @Override
    public FieldVector2<E> apply(FieldVector2<E> in) {
        return rotate(in);
    }

    @Override
    public Rotation2<E> reciprocal() {
        return new Rotation2<>(rot.reciprocal(), structure);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rotation2 rotation = (Rotation2) o;
        return getStructure().getEquivalence().test(this, rotation);
    }

    @Override
    public int hashCode() {
        return rot.hashCode();
    }

    @Override
    public String toString() {
        return rot.toString();
    }

}
