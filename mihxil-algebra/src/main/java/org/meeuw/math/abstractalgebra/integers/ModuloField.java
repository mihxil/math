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

import static org.meeuw.configuration.ReflectionUtils.getDeclaredBinaryMethod;

import org.meeuw.math.Example;
import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.ScalarField;
import org.meeuw.math.exceptions.FieldIncompleteException;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.*;
import org.meeuw.math.validation.Prime;

/**
 * The Galois field ℤ/pℤ.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(value = Field.class, string = "ℤ/pℤ")
public class ModuloField extends ModuloStructure<ModuloFieldElement, ModuloField>
    implements ScalarField<ModuloFieldElement> {




    private static final Map<Long, ModuloField> INSTANCES = new ConcurrentHashMap<>();

    public static ModuloField of(@Prime long divisor) throws InvalidElementCreationException {
        return INSTANCES.computeIfAbsent(divisor, ModuloField::new);
    }

    @Example(Field.class)
    public static final ModuloField Z3Z = of(3);

    private ModuloField(long divisor) throws InvalidElementCreationException {
        super(ModuloFieldElement.class, divisor);
        if (! IntegerUtils.isPrime(divisor)) {
            throw new InvalidElementCreationException(divisor + " is not a prime");
        }
    }


    @Override
    public ModuloFieldElement element(long v) {
        return new ModuloFieldElement(v, this);
    }

    @Override
    public ModuloFieldElement pi() {
        throw new FieldIncompleteException("pi cannot be approximated in a modulo field");
    }
}
