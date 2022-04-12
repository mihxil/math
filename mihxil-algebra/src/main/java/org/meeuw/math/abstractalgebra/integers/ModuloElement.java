/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.integers;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.RingElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public abstract class ModuloElement<E extends ModuloElement<E, S>, S extends ModuloStructure<E, S>>
    implements RingElement<E>, Serializable {

    //@Serial
    private static final long serialVersionUID = 0L;

    @Getter
    final int value;
    final S structure;

    ModuloElement(int value, S structure) {
        this.value = value % structure.divisor;
        this.structure = structure;
    }

    @Override
    public S getStructure() {
        return structure;
    }

    @Override
    public E times(E multiplier) {
        return structure.element(value * multiplier.value);
    }

    @Override
    public E plus(E summand) {
        return structure.element(value + summand.value);
    }

    @Override
    public E negation() {
        return structure.element(-1 * value );
    }

    @Override
    public String toString() {
        return String.valueOf(value); /* + "%" + structure.divisor;*/
    }

}
