module org.mihxil.demo {
    requires java.logging;

    requires org.meeuw.time;

    requires org.meeuw.math.statistics;
    requires org.meeuw.math.algebras;
    requires org.meeuw.math;
    requires org.meeuw.math.parser;

    requires org.meeuw.physics;
    requires org.meeuw.configuration;

    requires static lombok;
    requires static org.jspecify;

    exports org.meeuw.math.demo;

}
