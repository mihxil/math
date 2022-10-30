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
package org.meeuw.math.abstractalgebra.klein;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.Group;
import org.meeuw.math.abstractalgebra.GroupElement;

/**
 * @since 0.8
 */
public enum KleinElement implements GroupElement<KleinElement> {

    /**
     * The unity element.
     */
    e() {
        @Override
        public KleinElement operate(KleinElement multiplier) {
            return multiplier;
        }
    },
    a,
    b,
    c
    ;

    @Override
    public Group<KleinElement> getStructure() {
        return KleinGroup.INSTANCE;
    }

    @Override
    public KleinElement operate(KleinElement multiplier) {
        if (multiplier == e) {
            return this;
        }
        if (multiplier == this) {
            return e;
        }
        return Stream.of(a, b, c)
            .filter(el -> el != this && el != multiplier)
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    }

    @Override
    public KleinElement inverse() {
        return this;
    }


}
