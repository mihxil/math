package org.meeuw.math.abstractalgebra.bigdecimal;

import org.meeuw.math.abstractalgebra.Field;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class BigDecimalField implements Field<BigDecimalElement, BigDecimalField> {

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
