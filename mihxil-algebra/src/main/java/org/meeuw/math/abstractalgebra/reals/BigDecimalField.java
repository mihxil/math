package org.meeuw.math.abstractalgebra.reals;

import org.meeuw.math.abstractalgebra.NumberField;

/**
 * The algebra for {@link java.math.BigDecimal} (wrapped in {@link BigDecimalElement}
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalField implements NumberField<BigDecimalElement> {

    public static final BigDecimalField INSTANCE = new BigDecimalField();

    @Override
    public BigDecimalElement zero() {
        return BigDecimalElement.ZERO;
    }

    @Override
    public BigDecimalElement one() {
        return BigDecimalElement.ONE;
    }
}
