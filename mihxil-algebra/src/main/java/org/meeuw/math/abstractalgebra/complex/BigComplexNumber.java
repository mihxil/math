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
package org.meeuw.math.abstractalgebra.complex;

import java.io.Serializable;

import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.CompleteFieldElement;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalField;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Singleton
public class BigComplexNumber extends CompleteComplexNumber<BigComplexNumber, BigDecimalElement, BigDecimalField>
    implements
    CompleteFieldElement<BigComplexNumber>,
    MetricSpaceElement<BigComplexNumber, BigDecimalElement>,
    Serializable {

    private static final long serialVersionUID = 0L;

    public BigComplexNumber(BigDecimalElement real, BigDecimalElement imaginary) {
        super(real, imaginary);
    }

    public static BigComplexNumber of(BigDecimalElement real, BigDecimalElement imaginary) {
        return new BigComplexNumber(real, imaginary);
    }

    public static BigComplexNumber of(BigDecimalElement real) {
        return new BigComplexNumber(real, BigDecimalElement.ZERO);
    }


    public static BigComplexNumber of(String string) {
        String[] split = string.split("\\s+", 3);
        if (split.length == 3) {
            String rstring = split[0];
            String istring = split[2].substring(0, split[2].length() - 1);
            if (istring.equals("-")) {
                istring = "-1";
            }
            BigDecimalElement impart = BigDecimalElement.of(istring);
            if (split[1].equals("-")) {
                impart = impart.negation();
            }
            return of(BigDecimalElement.of(rstring), impart);
        }
        if (split[0].endsWith("i")) {
            String truncated = split[0].substring(0, split[0].length() - 1);
            if (truncated.isEmpty()) {
                return of(BigDecimalElement.ZERO, BigDecimalElement.ONE);
            }
            if (truncated.equals("-")) {
                return of(BigDecimalElement.ZERO, BigDecimalElement.ONE.negation());
            }
            return of(BigDecimalElement.ZERO, BigDecimalElement.of(split[0].substring(0, split[0].length() - 1)));
        } else {
            return of(BigDecimalElement.of(split[0]));
        }
    }


    @Override
    protected BigComplexNumber _of(BigDecimalElement real, BigDecimalElement imaginary) {
        return new BigComplexNumber(real, imaginary);
    }

    @Override
    public BigComplexNumbers getStructure() {
        return BigComplexNumbers.INSTANCE;
    }

}
