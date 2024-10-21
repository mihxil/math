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

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.validation.Prime;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(value = Field.class, string = "ℤ/nℤ")
public class ModuloField extends ModuloStructure<ModuloFieldElement, ModuloField>
    implements Field<ModuloFieldElement> {

    private static final Map<Integer, ModuloField> INSTANCES = new ConcurrentHashMap<>();

    public static ModuloField of(@Prime int divisor) throws InvalidElementCreationException {
        return INSTANCES.computeIfAbsent(divisor, ModuloField::new);
    }

    @Example(Field.class)
    public static final ModuloField Z3Z = of(3);

    private ModuloField(int divisor) throws InvalidElementCreationException {
        super(ModuloFieldElement.class, divisor);
        if (! IntegerUtils.isPrime(divisor)) {
            throw new InvalidElementCreationException("" + divisor + " is not a prime");
        }
    }

    @Override
    public ModuloFieldElement element(int v) {
        return new ModuloFieldElement(v, this);
    }


}
