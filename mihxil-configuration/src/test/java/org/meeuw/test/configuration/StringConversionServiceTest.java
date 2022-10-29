package org.meeuw.test.configuration;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.StringConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class StringConversionServiceTest {


    @Test
    public void intToString() {
        assertThat(StringConversionService.toString(1)).contains("1");

        assertThat(StringConversionService.fromString("1", Integer.TYPE)).contains(1);

        assertThat(StringConversionService.fromString("xx", Integer.TYPE)).isEmpty();

    }
    @Test
    public void fromInteger() {
        assertThat(StringConversionService.fromString("1", Integer.class)).contains(1);
    }

    @Test
    public void longToString() {
        assertThat(StringConversionService.toString(2L)).contains("2");

        assertThat(StringConversionService.fromString("2", Long.TYPE)).contains(2L);
        assertThat(StringConversionService.fromString("2", Long.class)).contains(2L);
    }

    @Test
    public void floatToString() {
        assertThat(StringConversionService.toString(1.0f)).contains("1.0");

        assertThat(StringConversionService.fromString("1.0", Float.TYPE)).contains(1.0f);
        assertThat(StringConversionService.fromString("1.0", Float.class)).contains(1.0f);
    }

    @Test
    public void doubleToString() {
        assertThat(StringConversionService.toString(1.0d)).contains("1.0");

        assertThat(StringConversionService.fromString("1.0", Double.TYPE)).contains(1.0d);
        assertThat(StringConversionService.fromString("1.0", Double.class)).contains(1.0d);
    }

    @Test
     public void booleanToString() {
        assertThat(StringConversionService.toString(true)).contains("true");

        assertThat(StringConversionService.fromString("true", Boolean.TYPE)).contains(Boolean.TRUE);
        assertThat(StringConversionService.fromString("false", Boolean.class)).contains(false);

        assertThat(StringConversionService.fromString("xxx", Boolean.class)).isEmpty();
    }

      @Test
     public void enumToString() {
        assertThat(StringConversionService.toString(A.x)).contains("x");

        assertThat(StringConversionService.fromString("y", A.class)).contains(A.y);
        assertThat(StringConversionService.fromString("xxx", A.class)).isEmpty();

    }

}
