module org.mihxil.demo {
    requires java.logging;

    requires org.meeuw.time;

    requires org.meeuw.math.statistics;
    requires org.meeuw.math.algebras;
    requires org.meeuw.math;
    requires org.meeuw.math.parser;
    requires static lombok;
    requires org.meeuw.physics;
    requires org.meeuw.configuration;

    exports org.meeuw.math.demo;

}
