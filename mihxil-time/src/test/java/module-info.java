module org.meeuw.time.test {
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive java.logging;

    requires transitive  org.meeuw.time;
    requires org.meeuw.functional;
    requires org.meeuw.math.abstractalgebra.test;
    requires org.checkerframework.checker.qual;

    opens org.meeuw.test.time.parser;
    opens org.meeuw.test.time;
}
