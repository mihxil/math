package org.meeuw.test.configuration;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.StringConversionService;
import org.meeuw.test.configuration.spi.TestConfigurationAspect;

import static org.assertj.core.api.Assertions.assertThat;

public class StringConversionServiceTest {


    @Test
    void intToString() {
        assertThat(StringConversionService.toString(1)).contains("1");

        assertThat(StringConversionService.fromString("1", Integer.TYPE)).contains(1);

        assertThat(StringConversionService.fromString("xx", Integer.TYPE)).isEmpty();

    }
    @Test
    void fromInteger() {
        assertThat(StringConversionService.fromString("1", Integer.class)).contains(1);
    }

    @Test
    void longToString() {
        assertThat(StringConversionService.toString(2L)).contains("2");

        assertThat(StringConversionService.fromString("2", Long.TYPE)).contains(2L);
        assertThat(StringConversionService.fromString("2", Long.class)).contains(2L);
    }

    @Test
    void floatToString() {
        assertThat(StringConversionService.toString(1.0f)).contains("1.0");

        assertThat(StringConversionService.fromString("1.0", Float.TYPE)).contains(1.0f);
        assertThat(StringConversionService.fromString("1.0", Float.class)).contains(1.0f);
    }

    @Test
    void doubleToString() {
        assertThat(StringConversionService.toString(1.0d)).contains("1.0");

        assertThat(StringConversionService.fromString("1.0", Double.TYPE)).contains(1.0d);
        assertThat(StringConversionService.fromString("1.0", Double.class)).contains(1.0d);
    }

    @Test
     void booleanToString() {
        assertThat(StringConversionService.toString(true)).contains("true");

        assertThat(StringConversionService.fromString("true", Boolean.TYPE)).contains(Boolean.TRUE);
        assertThat(StringConversionService.fromString("false", Boolean.class)).contains(false);

        assertThat(StringConversionService.fromString("xxx", Boolean.class)).isEmpty();
    }

    @Test
    void enumToString() {
        assertThat(StringConversionService.toString(A.x)).contains("x");

        assertThat(StringConversionService.fromString("y", A.class)).contains(A.y);
        assertThat(StringConversionService.fromString("xxx", A.class)).isEmpty();
    }

    @Test
    void someSerializable() {
        assertThat(StringConversionService.toString(new TestConfigurationAspect.SomeSerializable(1, "b"))).isEmpty();
    }

}
