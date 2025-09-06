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
package org.meeuw.test.configuration;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.configuration.ReflectionUtils.getDeclaredBinaryMethod;

@Log
class ReflectionUtilsTest {

    public static class A {
        private static final A a = new A();
        public static final A b = new A();
        public static final String c = "c";

        private static final String noconstant_d = "d";
        public String noconstant_e = "hoi";

        public A algebraic(A b) {
            return b;
        }
    }

    @Test
    public void forConstants() {
        List<A> result = new ArrayList<>();
        ReflectionUtils.forConstants(A.class, result::add);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(A.b);
    }

    @Test
    public void getBinary() {
        Method algebraic = getDeclaredBinaryMethod(A.class, "algebraic");
        assertThat(algebraic).isNotNull();

        assertThatThrownBy(() -> {
            getDeclaredBinaryMethod(A.class, "foobar");
        }).isInstanceOf(NoSuchMethodException.class);

    }
}
