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

import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.Scalar;

/**
 * Elements of {@link Squares ℤ2}
 * @author Michiel Meeuwissen
 * @since 0.18
 */
public class Square
    extends AbstractIntegerElement<Square, Square, Squares>
    implements
    MultiplicativeSemiGroupElement<Square>,
    Scalar<Square> {

    public static final Square ZERO = new Square(BigInteger.ZERO);
    public static final Square ONE = new Square(BigInteger.ONE);


    public static Square of(BigInteger value) throws InvalidElementCreationException {
        return Squares.INSTANCE.newElement(value);
    }
    public static Square of(@org.meeuw.math.validation.Square long value) throws InvalidElementCreationException {
        if (! IntegerUtils.isSquare(value)) {
            throw new InvalidElementCreationException("The argument must be a square (" + value + " isn't)");
        }
        return of(BigInteger.valueOf(value));
    }

    Square(BigInteger value) {
        super(Squares.INSTANCE, value);
    }

    @Override
    public Square times(Square multiplier) {
        return of(value.multiply(multiplier.value));
    }

    @Override
    public Square pow(@Positive int exponent) {
        if (exponent == 0) {
            throw new IllegalPowerException("Cannot raise to 0",  this + "^0");
        }
        return super.pow(exponent);
    }

    @Override
    public Square sqr() {
        return of(value.multiply(value));
    }

    @Override
    public Square abs() {
        return of(value.abs());
    }


}
