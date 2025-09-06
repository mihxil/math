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
    requires transitive java.logging;

    requires org.meeuw.math;
    requires lombok;
    requires org.reflections;
    requires org.meeuw.math.algebras;
    requires org.meeuw.math.abstractalgebra.test;
    requires org.meeuw.configuration;
    requires jakarta.validation;
    requires org.hibernate.validator;

    uses ConfigurationAspect;
    uses AlgebraicElementFormatProvider;

    uses jakarta.validation.spi.ValidationProvider;



}

