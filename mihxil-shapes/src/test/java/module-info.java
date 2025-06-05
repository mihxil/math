/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
open module org.meeuw.test.shapes {

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive org.apache.logging.log4j;


    requires lombok;
    requires org.meeuw.configuration;
    requires org.checkerframework.checker.qual;
    requires jakarta.validation;

    requires org.meeuw.functional;
    requires org.meeuw.math;
    requires org.meeuw.math.abstractalgebra.test;
    requires org.meeuw.math.algebras;
    requires org.meeuw.math.shapes;

    requires java.xml;


}

