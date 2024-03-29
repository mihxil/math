/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.configuration.spi;


import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.test.configuration.A;

/**
 * @author Michiel Meeuwissen
 */
public class TestConfigurationAspect implements ConfigurationAspect {

    @With
    @Getter
    final int someInt;

    @Getter
    final Integer someInteger;

    @With
    @Getter
    final long someLong;

    @With
    @Getter
    final float someFloat;

    @With
    @Getter
    final double someDouble;


    @With
    @Getter
    final Boolean someBoolean;

    @With
    @Getter
    final SomeSerializable someSerializable;


    @With
    @Getter
    final String someString;

    @With
    @Getter
    final A someEnum;


    @With
    @Getter
    final NotSerializable notSerializable;

    @With
    @Getter
    final boolean somePrimitiveBoolean;


    @lombok.Builder
    private TestConfigurationAspect(
        int someInt,
        Integer someInteger,
        long someLong,
        float someFloat,
        double someDouble,
        Boolean someBoolean,
        SomeSerializable someSerializable,
        String someString,
        A someEnum,
        NotSerializable notSerializable,
        boolean somePrimitiveBoolean) {
        this.someInt = someInt;
        this.someInteger = someInteger;
        this.someLong = someLong;
        this.someFloat = someFloat;
        this.someDouble = someDouble;
        this.someBoolean = someBoolean;
        this.someSerializable = someSerializable;
        this.someString = someString;
        this.someEnum = someEnum;
        this.notSerializable = notSerializable;
        this.somePrimitiveBoolean = somePrimitiveBoolean;
    }

    public TestConfigurationAspect() {
        this(-1, null, 100L, 3.14f, 2.71828182845904d, true,
            new SomeSerializable(1, "a"),
            "string",
            A.x,
            new NotSerializable(2, "b"),
            false
        );
    }

    @Override
    public List<Class<?>> associatedWith() {
        return Collections.singletonList(TestProvider.class);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @ToString
    public static class SomeSerializable implements Serializable {
        final int a;
        final String b;

        public SomeSerializable(int a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    @ToString
    public static class NotSerializable  {
        final int a;
        final String b;

        public NotSerializable(int a, String b) {
            this.a = a;
            this.b = b;
        }
    }
}
