import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.text.spi.AlgebraicElementFormatProvider;

/**
 * @author Michiel Meeuwissen

 */
open module org.meeuw.test.physics {

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive java.logging;

    requires org.meeuw.math;
    requires lombok;

    requires org.meeuw.math.abstractalgebra.test;
    requires org.meeuw.configuration;
    requires jakarta.validation;
    requires org.hibernate.validator;
    requires org.meeuw.physics;
    requires org.meeuw.functional;

    uses ConfigurationAspect;
    uses AlgebraicElementFormatProvider;

    uses jakarta.validation.spi.ValidationProvider;



}

