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

import lombok.extern.java.Log;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.configuration.*;
import org.meeuw.test.configuration.spi.*;
import org.meeuw.test.configuration.spi.TestConfigurationAspect.NotSerializable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.configuration.ConfigurationService.*;

@Log
public class ConfigurationServiceTest {

    @Test
    public void invalidConfigurationAspect() {
        Assertions.assertThatThrownBy(() -> getConfiguration().getAspect(Unregistered.class)).isInstanceOf(ConfigurationException.class);

    }

    @Test
    public void store() {
        long previous = getConfigurationAspect(TestConfigurationAspect.class).getSomeLong();
        log.info("previous " + Instant.ofEpochMilli(previous));
        defaultConfiguration((builder) -> {
            builder.configure(TestConfigurationAspect.class, (c) ->
                c.withSomeLong(System.currentTimeMillis())
                    .withSomeString("foobar")
            );
        });

    }

    @Test
    public void getAndSetConfiguration() {
        Configuration configuration = getConfiguration();
        TestConfigurationAspect aspect = configuration.getAspect(TestConfigurationAspect.class);
        int someInt = aspect.getSomeInt();
        log.info(() -> String.format("some int: %d", someInt));
        ConfigurationService.setConfiguration(configuration.toBuilder()
            .configure(TestConfigurationAspect.class, (nc) -> nc.withSomeInt(8))
            .build()
        );
        assertThat(getConfiguration()
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


        withConfiguration((con) -> con
                .configure(TestConfigurationAspect.class, (tc) -> tc.withSomeInt(3))
            , () -> {
                assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(3);

            });


        assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeInt()).isEqualTo(-1);


        final Configuration before = getConfiguration();
        assertThat(before).isEqualTo(getConfiguration());
        withAspect(TestConfigurationAspect.builder()
            .someDouble(1234d)
            .build(), () -> {
            assertThat(getConfigurationAspect(TestConfigurationAspect.class).getSomeDouble()).isEqualTo(1234d);
            assertThat(before).isNotEqualTo(getConfiguration());
        });
    }

    @Test
    public void invalid() {
        assertThatThrownBy(() -> {
            getConfiguration().toBuilder().aspectDefault(InvalidConfigurationAspect.class);
        }).isInstanceOf(InstantiationException.class);
    }


    @Test
    public void associatedWith() {
        List<ConfigurationAspect> configurationAspectsAssociatedWith = getConfiguration().getConfigurationAspectsAssociatedWith(TestProvider.class);
        assertThat(configurationAspectsAssociatedWith).hasSize(1);
        assertThat(configurationAspectsAssociatedWith.get(0)).isInstanceOf(TestConfigurationAspect.class);

    }

}
