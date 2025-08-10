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
package org.meeuw.math.abstractalgebra.trivial;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.AbelianRingElement;


/**
 * There is precisely one element in the {@link TrivialRing}, which is described here.
 * @author Michiel Meeuwissen
 * @since 0.8
 * @see TrivialRing
 */
public enum TrivialRingElement
    implements
    AbelianRingElement<TrivialRingElement>,
    Serializable {

    e;

    @Override
    public TrivialRing getStructure() {
        return TrivialRing.INSTANCE;
    }

    @Override
    public TrivialRingElement times(TrivialRingElement multiplier) {
        return this;
    }

    @Override
    public TrivialRingElement plus(TrivialRingElement summand) {
        return this;
    }

    @Override
    public TrivialRingElement negation() {
        return this;
    }

    @Override
    public TrivialRingElement operate(TrivialRingElement operand) {
        return this;
    }

    @Override
    public TrivialRingElement inverse() {
        return this;
    }

}
