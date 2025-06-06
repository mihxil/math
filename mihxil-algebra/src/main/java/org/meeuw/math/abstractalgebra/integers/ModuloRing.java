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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.AbelianRing;

/**
 * Implementation of ℤ/nℤ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloRing extends ModuloStructure<ModuloRingElement, ModuloRing> {

    private static final Map<Integer, ModuloRing> instances = new ConcurrentHashMap<>();

    public static ModuloRing of(int divisor) {
        return instances.computeIfAbsent(divisor, ModuloRing::new);
    }

    @Example(AbelianRing.class)
    public static ModuloRing Z8 = of(8);

    private ModuloRing(long divisor) {
        super(ModuloRingElement.class, divisor);
    }

    @Override
    ModuloRingElement element(long v) {
        return new ModuloRingElement(v, this);
    }



}
