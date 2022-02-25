import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.permutations.text.PermutationConfiguration;
import org.meeuw.math.abstractalgebra.permutations.text.PermutationFormatProvider;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.algebras {
    requires static java.validation;
    requires static lombok;
    requires static org.checkerframework.checker.qual;

    requires java.logging;
    requires org.meeuw.math;
    requires org.meeuw.configuration;


    exports org.meeuw.math.abstractalgebra.complex;
    exports org.meeuw.math.abstractalgebra.dim3;
    exports org.meeuw.math.abstractalgebra.integers;
    exports org.meeuw.math.abstractalgebra.permutations;
    exports org.meeuw.math.abstractalgebra.quaternions;
    exports org.meeuw.math.abstractalgebra.rationalnumbers;
    exports org.meeuw.math.abstractalgebra.reals;
    exports org.meeuw.math.abstractalgebra.strings;
    exports org.meeuw.math.abstractalgebra.vectorspace;
    exports org.meeuw.math.abstractalgebra.permutations.text;
    exports org.meeuw.math.abstractalgebra.linear;

    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with PermutationFormatProvider;

    provides ConfigurationAspect with PermutationConfiguration;

}

