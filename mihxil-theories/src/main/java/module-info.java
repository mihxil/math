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
/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.abstractalgebra.test {
    requires static lombok;
    requires org.meeuw.math;
    requires net.jqwik.api;
    requires org.assertj.core;
    requires org.apache.logging.log4j;
    requires org.junit.jupiter.api;
    requires org.checkerframework.checker.qual;
    requires org.junit.jupiter.params;
    requires org.meeuw.configuration;

    exports org.meeuw.theories;
    exports org.meeuw.theories.abstractalgebra;
    exports org.meeuw.theories.numbers;

    exports org.meeuw.test.assertj;


}

