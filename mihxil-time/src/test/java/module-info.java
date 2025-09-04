module org.meeuw.time.test {
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.jupiter.engine;
    requires transitive net.jqwik.api;
    requires transitive org.assertj.core;
    requires transitive org.apache.logging.log4j;

    requires transitive  org.meeuw.time;
    requires org.meeuw.functional;

    opens org.meeuw.test.time.parser;
    opens org.meeuw.test.time;
}
