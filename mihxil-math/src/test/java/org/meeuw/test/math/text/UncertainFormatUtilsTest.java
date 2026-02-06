package org.meeuw.test.math.text;

import java.text.DecimalFormat;

import org.junit.jupiter.api.Test;

import org.meeuw.math.text.configuration.NumberConfiguration;

class UncertainFormatUtilsTest {

    @Test
    public void valueAndError() {
        StringBuffer buffer = new StringBuffer();
        ///SplitNumber<Double> splitMean = new  SplitNumber<>(1000d, 0);
        DecimalFormat defaultNumberFormat = (DecimalFormat) NumberConfiguration.getDefaultNumberFormat().clone();

/*
        UncertainFormatUtils.valueAndError(
            buffer,
            NumberConfiguration.getDefaultNumberFormat(),
            new FieldPosition(0),
            null,//splitMean.coefficient,
             null,
            UncertaintyConfiguration.Notation.ROUND_VALUE

        );
        assertThat(buffer.toString()).isEqualTo("1000");*/
    }

}
