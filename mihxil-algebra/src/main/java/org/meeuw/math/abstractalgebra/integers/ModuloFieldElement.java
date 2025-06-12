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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.NonExact;
import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.exceptions.DivisionByZeroException;

/**
 * Elements of {@link ModuloField ℤ/nℤ}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloFieldElement
    extends ModuloElement<ModuloFieldElement, ModuloField>
    implements ScalarFieldElement<ModuloFieldElement>, Ordered<ModuloFieldElement> {

    ModuloFieldElement(long value, ModuloField structure) {
        super(value, structure);
    }

    @Override
    public ModuloFieldElement reciprocal() {
        if (value == 0) {
            throw new DivisionByZeroException("reciprocal of 0", "reciprocal(0)");
        }
        // https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
        long t = 0;
        long newt = 1;
        long r = getStructure().divisor;
        long newr = value;
        while (newr != 0) {
            long quotient = r / newr;
            long oldt = newt;
            newt = t - quotient * newt;
            t = oldt;
            long oldr = newr;
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
        return new ModuloFieldElement((int) (value * multiplier) % getStructure().divisor, structure);
    }
    @Override
    @NonExact
    public ModuloFieldElement times(double multiplier) {
        if (multiplier > 20) {
            return times(Math.round(multiplier));
        } else {
            return times(Math.round(multiplier * 1000)).dividedBy(1000);
        }

    }

    @Override
    public int compareTo(@NonNull ModuloFieldElement o) {
        return Long.compare(value, o.value);
    }

    @Override
    public double doubleValue() {
        return (double) value;
    }

    @Override
    public ModuloFieldElement abs() {
        return this;
    }
}
