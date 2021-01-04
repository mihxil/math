package org.meeuw.math.abstractalgebra.dim3;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class FieldVector3Test  {

    @Test
    public void abs() {
        FieldVector3<BigDecimalElement> v = FieldVector3.of(of(3), of(-4), of(0));
        assertThat(v.abs()).isEqualTo(of(5));
    }


    @Test
    public void times() {
        FieldVector3<BigDecimalElement> v = FieldVector3.of(of(3), of(-4), of(0));
        assertThat(v.times(of(1.5))).isEqualTo(FieldVector3.of(of(4.5), of(-6), of(0)));
    }


    @Test
    public void dividedBy() {
        FieldVector3<BigDecimalElement> v = FieldVector3.of(of(3), of(-4), of(0));
        assertThat(v.dividedBy(of(-2))).isEqualTo(FieldVector3.of(of(-1.5), of(2), of(0)));
    }

}
