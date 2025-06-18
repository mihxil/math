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
package org.meeuw.math.abstractalgebra.reals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumbers;
import org.meeuw.math.numbers.BigDecimalOperations;
import org.meeuw.math.numbers.MathContextConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * The algebra for {@link java.math.BigDecimal} (wrapped in {@link BigDecimalElement}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(CompleteScalarField.class)
@Singleton
public class BigDecimalField
    extends AbstractAlgebraicStructure<BigDecimalElement>
    implements
    CompleteScalarField<BigDecimalElement>,
    MetricSpace<BigDecimalElement, BigDecimalElement> {

    public static final BigDecimalField INSTANCE = new BigDecimalField();

    private BigDecimalField() {
        super(BigDecimalElement.class);
    }

    @Override
    public BigDecimalElement zero() {
        return BigDecimalElement.ZERO;
    }

    @Override
    public BigDecimalElement one() {
        return BigDecimalElement.ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.C;
    }

    @Override
    public Set<AlgebraicStructure<?>> getSuperGroups() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BigComplexNumbers.INSTANCE
        )));
    }

    public MathContext getMathContext() {
        return MathContextConfiguration.get().getContext();
    }

    @Override
    public String toString() {
        return "ℝ";
    }

    public BigDecimalElement atan2(BigDecimalElement y, BigDecimalElement x) {
        UncertainNumber<BigDecimal> uncertainNumber = BigDecimalOperations.INSTANCE.atan2(y.getValue(), x.getValue());
        return new BigDecimalElement(uncertainNumber.getValue(), uncertainNumber.getUncertainty());
    }

    @Override
    public BigDecimalElement pi() {
        return BigDecimalElement.PI;
    }

    @Override
    public BigDecimalElement e() {
        return BigDecimalElement.e;
    }
    @Override
    public BigDecimalElement nextRandom(Random random) {
        return BigDecimalElement.of(BigDecimal.valueOf(random.nextDouble()));
    }

    @Override
    public BigDecimalElement 𝜑() {
        return (BigDecimalElement.ONE.plus(BigDecimalElement.of(5).sqrt())).dividedBy(2);
    }
}
