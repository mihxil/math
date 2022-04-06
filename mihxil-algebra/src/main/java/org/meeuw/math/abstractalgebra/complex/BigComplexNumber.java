package org.meeuw.math.abstractalgebra.complex;

import java.io.Serializable;

import org.meeuw.math.abstractalgebra.CompleteFieldElement;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.BigDecimalField;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class BigComplexNumber extends CompleteComplexNumber<BigComplexNumber, BigDecimalElement, BigDecimalField>
    implements
    CompleteFieldElement<BigComplexNumber>,
    MetricSpaceElement<BigComplexNumber, BigDecimalElement>,
    Serializable {

    static final long serialVersionUID = 0L;

    public BigComplexNumber(BigDecimalElement real, BigDecimalElement imaginary) {
        super(real, imaginary);
    }

    public static BigComplexNumber of(BigDecimalElement real, BigDecimalElement imaginary) {
        return new BigComplexNumber(real, imaginary);
    }

    public static BigComplexNumber of(BigDecimalElement real) {
        return new BigComplexNumber(real, BigComplexNumbers.INSTANCE.getElementStructure().zero());
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
