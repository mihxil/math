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
package org.meeuw.math.abstractalgebra.bool;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.AbelianRingElement;

/**
 * Element of the Boolean-group.
 * @since 0.20
 */
public enum BooleanElement implements AbelianRingElement<BooleanElement>, BooleanSupplier, IntSupplier {


    TRUE,
    FALSE
    ;

    public static BooleanElement of(boolean value) {
        return value ? TRUE : FALSE;
    }
    public static BooleanElement of(int value) {
        return of(value != 0);
    }

    @Override
    public @NonNull BooleanRing getStructure() {
        return BooleanRing.INSTANCE;
    }

    @Override
    public BooleanElement operate(BooleanElement multiplier) {
        return times(multiplier);
    }

    @Override
    public BooleanElement negation() {
        return this;
    }

    @Override
    public BooleanElement times(BooleanElement multiplier) {
        return and(multiplier);
    }

    public BooleanElement and(BooleanElement multiplier) {
        return of(getAsBoolean() && multiplier.getAsBoolean());
    }

    public BooleanElement or(BooleanElement multiplier) {
        return of(getAsBoolean() || multiplier.getAsBoolean());
    }

    @Override
    public BooleanElement plus(BooleanElement addend) {
        return xor(addend);
    }

    public BooleanElement xor(BooleanElement addend) {
        return of(getAsBoolean() ^ addend.getAsBoolean());
    }

    public BooleanElement nand(BooleanElement multiplier) {
        return and(multiplier).not();
    }

    public BooleanElement not() {
        return of(! getAsBoolean());
    }


    @Override
    public boolean getAsBoolean() {
        return this == TRUE;
    }

    @Override
    public int getAsInt() {
        return this == TRUE ? 1 : 0;
    }

    @Override
    public String toString() {
        return getAsBoolean() ? "⊤" : "⊥";
    }
}
