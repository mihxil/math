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
package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
class ReflectionUtilsTest {

    public static class A {
        private static final A a = new A();
        public static final A b = new A();
        public static final String c = "c";
    }

    @Test
    public void forConstants() {
        List<A> result = new ArrayList<>();
        ReflectionUtils.forConstants(A.class, result::add);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(A.b);
    }

}
