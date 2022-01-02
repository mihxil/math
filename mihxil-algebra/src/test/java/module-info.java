import org.meeuw.configuration.ConfigurationAspect;
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
    requires org.reflections;
    requires org.meeuw.math.algebras;
    requires org.meeuw.math.abstractalgebra.test;

    uses ConfigurationAspect;
    uses AlgebraicElementFormatProvider;


}

