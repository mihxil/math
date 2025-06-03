package org.meeuw.test.math.shapes.d2;

import org.junit.jupiter.api.Test;

import org.meeuw.math.shapes.d2.NGon;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;


public class NGonTest {


    @Test
    public void triangle() {

        NGon<UncertainReal> triangle = new NGon<>(3, exactly(1.0));

        assertThat(triangle.n()).isEqualTo(3);
        //assertThat(triangle.area()).eq(exactly(Math.sqrt(3)/ 4));
    }
}
