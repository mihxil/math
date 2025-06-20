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
 * @since 0.15
 */
module org.meeuw.math.shapes {

    requires static lombok;
    requires static org.checkerframework.checker.qual;
    requires static jakarta.validation;

    requires java.logging;
    requires java.xml;

    requires org.meeuw.math;
    requires org.meeuw.math.algebras;
    requires jdk.xml.dom;

    exports org.meeuw.math.shapes.dim2;
    exports org.meeuw.math.shapes.dim3;
    exports org.meeuw.math.svg;
    exports org.meeuw.math.shapes;

}
