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
package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * A number with an uncertainty {@link #doubleUncertainty()}
 *
 * See <a href="http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf">this url</a>
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class AbstractUncertainDouble
    extends Number
    implements
    UncertainScalar<Double, UncertainReal>,
    Comparable<UncertainReal>,
    UncertainDouble<UncertainReal> {

    @Override
    public int compareTo(@NonNull UncertainReal o) {
        if (equals(o)) {
            return 0;
        }
        return UncertainDouble.super.compareTo(o);
    }

    @Override
    public int intValue() {
        return (int) Math.round(doubleValue());
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(doubleValue());
    }


}
