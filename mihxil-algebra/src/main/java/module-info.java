import org.meeuw.math.abstractalgebra.permutations.text.PermutationFormatProvider;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.algebras {
    exports org.meeuw.math.abstractalgebra.complex;
    exports org.meeuw.math.abstractalgebra.dim3;
    exports org.meeuw.math.abstractalgebra.integers;
    exports org.meeuw.math.abstractalgebra.permutations;
    exports org.meeuw.math.abstractalgebra.rationalnumbers;
    exports org.meeuw.math.abstractalgebra.reals;

    requires static java.validation;
    requires org.meeuw.math;
    requires static lombok;

    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with

        PermutationFormatProvider;

}

