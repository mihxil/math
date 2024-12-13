package org.meeuw.test.math.abstractalgebra.padic;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.padic.PAdicInteger;
import org.meeuw.math.abstractalgebra.padic.PAdicIntegers;

@Log4j2
class PadicIntegersTest { //implements FieldTheory<PAdicInteger> {

    PAdicIntegers p5 = PAdicIntegers.of(5);

    @Test
    public void simple() {
        PAdicInteger a = p5.of("0", "234");
        PAdicInteger b = p5.of("0", "340");

        PAdicInteger c = a.p(b);

        System.out.printf("\n %s\n %s\n---------+\n%s%n", a, b, c);

        log.info("{} + {} = {}", a.bigIntegerValue(), b.bigIntegerValue(), c.bigIntegerValue());
    }


}
