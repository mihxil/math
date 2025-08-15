package org.meeuw.test.math.arithmetic.ast;

import org.junit.jupiter.api.Test;

import org.meeuw.math.arithmetic.ast.Parser;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.reals.RealField;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {


    Parser<RealNumber, RealField> parser = new Parser<>(RealField.INSTANCE);
    @Test
    public void test() {
        assertThat(parser.parse("2 * (3 + 5)").eval().getValue()).isEqualTo(2 * (3 + 5));

        assertThat(parser.parse("2 * (3 - 5)").eval().getValue()).isEqualTo(2 * (3 - 5));

    }
}
