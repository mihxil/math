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
package org.meeuw.math.abstractalgebra.trivial;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.GroupElement;


/**
 * There is precisely one element in the {@link TrivialGroup}, which is described here.
 * An {@link Enum} with one value.
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public enum TrivialGroupElement implements
    GroupElement<TrivialGroupElement>,
    Serializable {

    /**
     * The one value.
     */
    e;

    @Override
    public TrivialGroup getStructure() {
        return TrivialGroup.INSTANCE;
    }

    /**
     * The trivial group defines one operation, which always returns {@link #e}
     * @return {@code this}
     */
    @Override
    public TrivialGroupElement operate(TrivialGroupElement operand) {
        return this;
    }

    /**
     * The inverse of the only element {@link #e} is {@link #e} to. (Since e * e = 1 and (e == 1))
     */
    @Override
    public TrivialGroupElement inverse() {
        return this;
    }

}
