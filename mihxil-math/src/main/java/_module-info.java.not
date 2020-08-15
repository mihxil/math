import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.text.spi.DefaultUncertainNumberFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math {
    requires static lombok;
    requires static java.validation;
    exports org.meeuw.math.abstractalgebra;
    exports org.meeuw.math.text;
    exports org.meeuw.math.uncertainnumbers;
    exports org.meeuw.math;
    exports org.meeuw.math.text.spi;

    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with
        DefaultUncertainNumberFormatProvider;

}

