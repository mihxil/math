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
package org.meeuw.math.abstractalgebra.quaternions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.GaussianRational;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.streams.StreamUtils;

/**
 * The division ring of quaternions ℍ.
 * @author Michiel Meeuwissen
 * @since 0.4
 * @see org.meeuw.math.abstractalgebra.quaternions
 * @see Quaternion
 */
@Log
public class Quaternions<E extends ScalarFieldElement<E>>
    extends AbstractAlgebraicStructure<Quaternion<E>>
    implements DivisionRing<Quaternion<E>>, Streamable<Quaternion<E>> {

    private static final Map<ScalarField<?>, Quaternions<?>> INSTANCES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <E extends ScalarFieldElement<E>> Quaternions<E> of(ScalarField<E> numberFieldElement) {
        return (Quaternions<E>) INSTANCES.computeIfAbsent(numberFieldElement, k -> {
            Quaternions<E> result = new Quaternions<>(numberFieldElement);
            log.info("Created new instance of " + result);
            return result;

            }
        );
    }

    @Example(value = DivisionRing.class, prefix = "Quaternions ")
    public static final Quaternions<RationalNumber> H_Q = of(RationalNumbers.INSTANCE);

    @Getter
    private final ScalarField<E> elementStructure;

    private final Quaternion<E> zero;
    private final Quaternion<E> one;
    private final Quaternion<E> i;
    private final Quaternion<E> j;
    private final Quaternion<E> k;

    private Quaternions(ScalarField<E> elementStructure) {
        super((Class) Quaternion.class);
        this.elementStructure = elementStructure;
        E z = this.elementStructure.zero();
        E u = this.elementStructure.one();
        this.zero = new Quaternion<>(z, z, z, z);
        this.one  = new Quaternion<>(u, z, z, z);
        this.i    = new Quaternion<>(z, u, z, z);
        this.j    = new Quaternion<>(z, z, u, z);
        this.k    = new Quaternion<>(z, z, z, u);
    }

    @Override
    public Cardinality getCardinality() {
        return elementStructure.getCardinality().pow(4);
    }

    @Override
    public Quaternion<E> zero() {
        return zero;
    }

    @Override
    public Quaternion<E> one() {
        return one;
    }

    public Quaternion<E> i() {
        return i;
    }

    public Quaternion<E> j() {
        return j;
    }

    public Quaternion<E> k() {
        return k;
    }

    @Override
    public String toString() {
        return "ℍ(" + elementStructure + ")";
    }

    @Override
    public Stream<Quaternion<E>> stream() {
        if (! (elementStructure instanceof Streamable<?>)) {
            throw new NotStreamable("Element structure " + elementStructure + " of " + this + " is not streamable");
        }
        return StreamUtils.nCartesianStream(4, () -> ((Streamable<E>) getElementStructure())
            .stream()
        ).map(earray -> new Quaternion<>(earray[0], earray[1], earray[2], earray[3]));
    }

    @Override
    public Quaternion<E> nextRandom(Random random) {
        return new Quaternion<>(
            elementStructure.nextRandom(random),
            elementStructure.nextRandom(random),
            elementStructure.nextRandom(random),
            elementStructure.nextRandom(random)
        );
    }

      static Pattern SPLIT_PATTERN = Pattern.compile("([+-]?)\\s*([^+-]+)");

    @Override
    public Quaternion<E> parse(String s) {
        Matcher matcher = SPLIT_PATTERN.matcher(s.trim());

        E real = elementStructure.zero();
        E i = elementStructure.zero();
        E j = elementStructure.zero();
        E k = elementStructure.zero();
        E one = elementStructure.one();

        while (matcher.find()) {
            String sign = matcher.group(1);
            String term = matcher.group(2).trim();
            int factor = sign.equals("+") || sign.isEmpty() ? 1 : -1;

            if (term.endsWith("i")) {
                String coeff = term.substring(0, term.length() - 1).trim();
                i = i.plus(parseCoefficient(coeff, factor));
            } else if (term.endsWith("j")) {
                String coeff = term.substring(0, term.length() - 1).trim();
                j = j.plus(parseCoefficient(coeff, factor));
            } else if (term.endsWith("k")) {
                String coeff = term.substring(0, term.length() - 1).trim();
                k = k.plus(parseCoefficient(coeff, factor));
            } else {
                real = real.plus(parseCoefficient(term.trim(), factor));
            }
        }
        return new Quaternion<>(real, i, j, k);
    }


    private E parseCoefficient(String coeff, int factor) {
        if (coeff.isEmpty()) {
            return elementStructure.one().times(factor);
        } else {
            return elementStructure.parse(coeff).times(factor);
        }
    }


}
