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
package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * See <a href="http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf">this url</a>
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class AbstractUncertainDouble<D extends UncertainDouble<D>>
    extends Number
    implements Comparable<D>, UncertainDouble<D> {



    @Override
    public int compareTo(@NonNull D o) {
        if (equals(o)) {
            return 0;
        }
        return Double.compare(doubleValue(), o.getValue());
    }


    @Override
    public int intValue() {
        return UncertainDouble.super.intValue();
    }

    @Override
    public float floatValue() {
        return UncertainDouble.super.floatValue();
    }

    @Override
    public double doubleValue() {
        return getValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(doubleValue());
    }

    @Override
    public D plus(D summand) {
        return UncertainDouble.super.plus(summand);
    }


}
