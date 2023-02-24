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
package org.meeuw.physics;

import lombok.Getter;

/**
 * Representation of a basic physical dimension. This follows the SI recommendation.
 *
 * @see DimensionalAnalysis
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public enum Dimension implements DimensionExponent {

    L("length"),
    M("mass"),
    T("time"),
    I("electric current"),
    Θ('\u0398', "thermodynamic temperature"),
    N("amount of substance"),
    J("luminous intensity");

    /**
     * Just an alias for if you can't type greek
     */
    static final Dimension TH = Θ;

    static final int NUMBER = values().length;

    final String toString;

    @Getter
    final String name;

    Dimension(char i, String name) {
        toString = String.valueOf(i);
        this.name = name;
    }

    Dimension(String name) {
        this.name = name;
        toString = name();
    }

    @Override
    public String toString() {
        return toString;
    }

    @Override
    public Dimension getDimension() {
        return this;
    }

    @Override
    public int getExponent() {
        return 1;
    }

    public SIUnit getSIUnit() {
        return SIUnit.valueOf(this);
    }
}
