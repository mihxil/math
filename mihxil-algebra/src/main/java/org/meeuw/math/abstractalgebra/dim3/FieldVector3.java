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
package org.meeuw.math.abstractalgebra.dim3;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static java.math.BigDecimal.ZERO;

/**
 * A 3-dimension vector on a {@link ScalarField}.
 *
 * @author Michiel Meeuwissen
 */
public class FieldVector3<E extends ScalarFieldElement<E,C>, C extends CompleteScalarFieldElement<C>>
    extends AbstractFieldVector3<E, C, FieldVector3<E, C>> {

    public static <E extends ScalarFieldElement<E,C>, C extends CompleteScalarFieldElement<C>> FieldVector3<E, C> of(E x, E y, E z) {
        return new FieldVector3<>(x, y, z);
    }

    public static FieldVector3<RealNumber, RealNumber> of(double x, double y, double z) {
        return new FieldVector3<>(RealNumber.of(x), RealNumber.of(y), RealNumber.of(z));
    }

    public static FieldVector3<BigDecimalElement, BigDecimalElement> of(BigDecimal x, BigDecimal y, BigDecimal z) {
        return new FieldVector3<>(new BigDecimalElement(x, ZERO), new BigDecimalElement(y, ZERO), new BigDecimalElement(z, ZERO));
    }

    public FieldVector3(E x, E y, E z) {
        super(x, y, z);
    }

    @Override
    public FieldVector3Space<E, C> getSpace() {
        return FieldVector3Space.of(x.getStructure());
    }

    FieldVector3<E, C> _of(E x, E y, E z) {
        return of(x, y, z);
    }


}
