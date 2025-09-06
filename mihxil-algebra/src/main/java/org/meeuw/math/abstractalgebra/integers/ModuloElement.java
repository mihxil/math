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
package org.meeuw.math.abstractalgebra.integers;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

import static org.meeuw.math.IntegerUtils.modPow;

import org.meeuw.math.abstractalgebra.AbelianRingElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public abstract class ModuloElement<E extends ModuloElement<E, S>, S extends ModuloStructure<E, S>>
    implements AbelianRingElement<E>, Serializable {

    //@Serial
    private static final long serialVersionUID = 0L;

    @Getter
    final long value;
    final S structure;

    ModuloElement(long value, S structure) {
        this.value = ((value % structure.divisor) + structure.divisor) % structure.divisor;
        assert this.value >= 0;
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

    public E pow(E exponent) {
        return structure.element(modPow(value, exponent.value, structure.divisor));
    }

    @Override
    public String toString() {
        return String.valueOf(value); /* + "%" + structure.divisor;*/
    }

}
