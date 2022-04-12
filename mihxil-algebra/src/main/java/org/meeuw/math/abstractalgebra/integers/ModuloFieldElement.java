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
package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.exceptions.DivisionByZeroException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloFieldElement
    extends ModuloElement<ModuloFieldElement, ModuloField>
    implements FieldElement<ModuloFieldElement> {

    ModuloFieldElement(int value, ModuloField structure) {
        super(value, structure);
    }

    @Override
    public ModuloFieldElement reciprocal() {
        if (value == 0) {
            throw new DivisionByZeroException("reciprocal of 0");
        }
        // https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
        int t = 0;
        int newt = 1;
        int r = getStructure().divisor;
        int newr = value;
        while (newr != 0) {
            int quotient = r / newr;
            int oldt = newt;
            newt = t - quotient * newt;
            t = oldt;
            int oldr = newr;
            newr = r - quotient * newr;
            r = oldr;
        }

        assert r <= 1; // the divisor is prime, so this should always have been possible

        if (t < 0) {
            t = t + getStructure().divisor;
        }

        return new ModuloFieldElement(t, structure);
    }

    @Override
    public ModuloFieldElement dividedBy(long divisor) {
        return times(new ModuloFieldElement((int) divisor % structure.divisor, structure).reciprocal());
    }

    @Override
    public ModuloFieldElement times(long multiplier) {
        return new ModuloFieldElement((int) ((long) value * multiplier) % getStructure().divisor, structure);
    }
}
