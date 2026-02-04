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
package org.meeuw.math.text;

import ch.randelshofer.fastdoubleparser.JavaDoubleParser;

import org.meeuw.math.abstractalgebra.reals.DoubleElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.numbers.Factor;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
public class UncertainNumberFormat<N extends Number> extends AbstractUncertainFormat<UncertainNumber<?>, RealNumber, N> {

    public UncertainNumberFormat() {
        super(UncertainNumber.class, null, UncertaintyConfiguration.DEFAULT_STRIP_ZEROS);
    }

    @Override
    RealNumber of(String v, String uncertainty, Factor factor) {
        return DoubleElement.of(
            JavaDoubleParser.parseDouble(v),
            JavaDoubleParser.parseDouble(uncertainty)
        );
    }

}
