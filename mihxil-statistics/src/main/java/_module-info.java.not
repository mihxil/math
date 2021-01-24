import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;
import org.meeuw.statistics.text.spi.StatisticalLongNumberFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.statistics {
    requires static lombok;
    requires java.logging;
    requires org.meeuw.math;

    exports org.meeuw.statistics;
    exports org.meeuw.statistics.text;


    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with
        StatisticalLongNumberFormatProvider;

}

