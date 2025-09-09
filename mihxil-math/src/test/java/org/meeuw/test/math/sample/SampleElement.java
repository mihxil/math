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
package org.meeuw.test.math.sample;

import java.util.function.BinaryOperator;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.AdditiveGroupElement;

public class SampleElement implements AdditiveGroupElement<SampleElement> {

    public static ThreadLocal<BinaryOperator<SampleElement>> PLUS = ThreadLocal.withInitial(() -> (a, b) -> a);


    private final BinaryOperator<SampleElement> p;

    public SampleElement() {
        this(PLUS.get());
    }

    public SampleElement(BinaryOperator<SampleElement> p) {
        this.p = p;
    }

    @Override
    public @NonNull SampleStructure getStructure() {
        return new SampleStructure();
    }

    @Override
    public SampleElement plus(SampleElement summand) {
        return p.apply(this, summand);
    }

    @Override
    public SampleElement negation() {
        return null;
    }

    @Override
    public String toString() {
        return "sampleelement";
    }

}
