package org.meeuw.math.abstractalgebra.reals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import org.meeuw.math.Example;
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
public class BigDecimalField
    extends AbstractAlgebraicStructure<BigDecimalElement>
    implements
    CompleteScalarField<BigDecimalElement>,
    MetricSpace<BigDecimalElement, BigDecimalElement> {

    public static final BigDecimalField INSTANCE = new BigDecimalField();

    protected BigDecimalField() {
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
}
