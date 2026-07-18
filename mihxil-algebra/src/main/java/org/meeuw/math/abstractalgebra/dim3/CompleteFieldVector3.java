/*
 *  Copyright 2026 Michiel Meeuwissen
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

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * A 3-dimension vector on a {@link CompleteField}.
 *
 * @author Michiel Meeuwissen
 * @since 0.20
 */
public class CompleteFieldVector3<C extends CompleteScalarFieldElement<C>> extends AbstractFieldVector3<C, C, CompleteFieldVector3<C>> {

    public CompleteFieldVector3(C x, C y, C z) {
        super(x, y, z);
    }

    public static <E  extends CompleteScalarFieldElement<E>> CompleteFieldVector3<E> of(E x, E y, E z) {
        return new CompleteFieldVector3<>(x, y, z);
    }

    public static CompleteFieldVector3<RealNumber> of(double x, double y, double z) {
        return new CompleteFieldVector3<>(RealNumber.of(x), RealNumber.of(y), RealNumber.of(z));
    }

    @Override
    public CompleteFieldVector3Space<C> getSpace() {
        return CompleteFieldVector3Space.of(x.getStructure());
    }

    @Override
    CompleteFieldVector3<C> _of(C x, C y, C z) {
        return of(x, y, z);
    }
}
