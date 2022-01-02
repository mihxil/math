/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
module org.meeuw.math.abstractalgebra.test {
    requires static lombok;
    requires org.meeuw.math;
    requires net.jqwik.api;
    requires org.assertj.core;
    requires org.apache.logging.log4j;
    requires org.junit.jupiter.api;
    exports org.meeuw.math.abstractalgebra.test;
    exports org.meeuw.math.numbers.test;
    exports org.meeuw.util.test;

}

