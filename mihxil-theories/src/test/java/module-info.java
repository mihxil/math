
/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
open module org.meeuw.theories.test {

    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive org.apache.logging.log4j;

    requires org.meeuw.math;
    requires lombok;
    requires org.meeuw.configuration;
    requires org.checkerframework.checker.qual;
}

