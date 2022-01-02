import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
open module org.meeuw.test {

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive org.apache.logging.log4j;

    requires org.meeuw.math;
    requires lombok;

    exports org.meeuw.test.math.text.spi.test;


    uses AlgebraicElementFormatProvider;

    provides AlgebraicElementFormatProvider with org.meeuw.test.math.text.spi.test.TestFormatProvider;

}

