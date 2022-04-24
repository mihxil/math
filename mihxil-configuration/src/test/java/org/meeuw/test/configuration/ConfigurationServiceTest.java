/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.configuration;

import lombok.Getter;
import lombok.With;
import lombok.extern.java.Log;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.configuration.*;
import org.meeuw.test.configuration.spi.TestConfigurationAspect;
import org.meeuw.test.configuration.spi.TestConfigurationAspect.NotSerializable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.configuration.ConfigurationService.*;

@Log
public class ConfigurationServiceTest {

    public static class Unregistered implements ConfigurationAspect {

        @Override
        public List<Class<?>> associatedWith() {
            return Collections.emptyList();
        }
    }
    public enum A {
        x,
        y
    }

    public static class TestConfiguration implements ConfigurationAspect {

        @Getter
        @With
        private final long integer;

        @Getter
        @With
        private final String string;

        @Getter
        @With
        private final A enumeration;

        public TestConfiguration() {
            this(100, "foobar", A.x);
        }

        @lombok.Builder
        private TestConfiguration(long integer, String string, A enumeration) {
            this.integer = integer;
            this.string = string;
            this.enumeration = enumeration;
        }


        @Override
        public List<Class<?>> associatedWith() {
            return Collections.singletonList(ConfigurationServiceTest.class);
        }
    }

    @Test
    public void invalidConfigurationAspect() {
        Assertions.assertThatThrownBy(() -> ConfigurationService.getConfiguration().getAspect(Unregistered.class)).isInstanceOf(ConfigurationException.class);

    }

    @Test
    public void store() {
        long previous = getConfigurationAspect(TestConfiguration.class).getInteger();
        log.info("previous " + Instant.ofEpochMilli(previous));
        defaultConfiguration((builder) -> {
            builder.configure(TestConfiguration.class, (c) ->
                c.withInteger(System.currentTimeMillis())
                    .withString("foobar")
            );
        });



    }


    @Test
    public void getAndSetConfiguration() {
        Configuration configuration = ConfigurationService.getConfiguration();
        TestConfigurationAspect aspect = configuration.getAspect(TestConfigurationAspect.class);
        int minimalExponent = aspect.getSomeInt();
        ConfigurationService.setConfiguration(configuration.toBuilder()
            .configure(TestConfigurationAspect.class, (nc) -> nc.withSomeInt(8))
            .build()
        );
        assertThat(ConfigurationService.getConfiguration()
            .getAspectValue(TestConfigurationAspect.class, TestConfigurationAspect::getSomeInt)
        ).isEqualTo(8);

    }


    @Test
    public void testConfigurationAspects() {
        defaultConfiguration((con) -> con
            .configure(TestConfigurationAspect.class,
                c -> c
                    .withSomeInt(4)
                    .withSomeSerializable(null)
                    .withNotSerializable(new NotSerializable(3, "x"))
            )
        );
        assertThat(getConfigurationAspect(TestConfigurationAspect.class)
            .getSomeInt()).isEqualTo(4);

        resetToDefaultDefaults();

        assertThat(ConfigurationService.sync()).isTrue();


        withAspect(TestConfigurationAspect.class,
            (b) -> b.withSomeInt(6), () -> {
                assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt())
                    .isEqualTo(6);
            }
        );
        assertThat(getConfigurationAspect(TestConfigurationAspect.class)
            .getSomeInt()).isEqualTo(-1);

        defaultConfiguration((con) -> con.configure(TestConfigurationAspect.class,
            c -> c.withSomeInt(5))
        );
        assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(5);


        resetToDefaultDefaults();

        assertThat(getConfigurationAspect(TestConfigurationAspect.class)
            .getSomeInt()).isEqualTo(-1);


        ConfigurationService.withConfiguration((con) -> con
                .configure(TestConfigurationAspect.class, (tc) -> tc.withSomeInt(3))
        , () -> {
                assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(3);

        });


        assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(-1);
    }


}
