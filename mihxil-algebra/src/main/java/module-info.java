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
import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.integers.Factoriable.Configuration;
import org.meeuw.math.abstractalgebra.permutations.text.PermutationConfiguration;
import org.meeuw.math.abstractalgebra.permutations.text.PermutationFormatProvider;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.algebras {
    requires static jakarta.validation;
    requires static lombok;
    requires static org.checkerframework.checker.qual;

    requires org.meeuw.math;
    requires org.meeuw.configuration;

    exports org.meeuw.math.abstractalgebra.complex;
    exports org.meeuw.math.abstractalgebra.dim3;
    exports org.meeuw.math.abstractalgebra.integers;
    exports org.meeuw.math.abstractalgebra.permutations;
    exports org.meeuw.math.abstractalgebra.quaternions;
    exports org.meeuw.math.abstractalgebra.quaternions.q8;
    exports org.meeuw.math.abstractalgebra.rationalnumbers;
    exports org.meeuw.math.abstractalgebra.bigdecimals;
    exports org.meeuw.math.abstractalgebra.strings;
    exports org.meeuw.math.abstractalgebra.trivial;
    exports org.meeuw.math.abstractalgebra.vectorspace;
    exports org.meeuw.math.abstractalgebra.permutations.text;
    exports org.meeuw.math.abstractalgebra.polynomial;
    exports org.meeuw.math.abstractalgebra.linear;
    exports org.meeuw.math.abstractalgebra.klein;
    exports org.meeuw.math.abstractalgebra.dihedral;
    exports org.meeuw.math.abstractalgebra.dim2;
    exports org.meeuw.math.abstractalgebra.padic;
    exports org.meeuw.math.abstractalgebra.padic.impl;

    opens org.meeuw.math.abstractalgebra.dim3 to org.hibernate.validator;
    opens org.meeuw.math.abstractalgebra.dim2 to org.hibernate.validator;
    opens org.meeuw.math.abstractalgebra.strings to org.hibernate.validator;
    opens org.meeuw.math.abstractalgebra.linear to org.hibernate.validator;
    opens org.meeuw.math.abstractalgebra.rationalnumbers to org.hibernate.validator;


    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with PermutationFormatProvider;

    provides ConfigurationAspect with PermutationConfiguration, Configuration;

}

