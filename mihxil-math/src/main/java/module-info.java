import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math {

    requires static lombok;
    requires static java.validation;

    requires java.logging;
    requires ch.obermuhlner.math.big;

    exports org.meeuw.math.abstractalgebra;
    exports org.meeuw.math.text;
    exports org.meeuw.math.uncertainnumbers;
    exports org.meeuw.math.uncertainnumbers.field;
    exports org.meeuw.math.exceptions;
    exports org.meeuw.math;
    exports org.meeuw.math.text.spi;
    exports org.meeuw.math.text.configuration;
    exports org.meeuw.math.numbers;

    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with UncertainDoubleFormatProvider;

}

