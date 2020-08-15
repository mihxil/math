import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.statistics.text.spi.StatisticalLongNumberFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.statistics {
    exports org.meeuw.statistics;
    exports org.meeuw.statistics.text;

    requires static org.checkerframework.checker.qual;
    requires static lombok;
    requires org.meeuw.math;
    requires com.google.common;
    requires java.logging;

    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with
        StatisticalLongNumberFormatProvider;

}

