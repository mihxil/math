package org.meeuw.test.math.arithmetic.ast;

import org.junit.jupiter.api.Test;

import org.meeuw.math.arithmetic.ast.Parser;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.math.uncertainnumbers.field.UncertainRealField;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {


    Parser<UncertainReal, UncertainRealField> parser = new Parser<>(UncertainRealField.INSTANCE);
    @Test
    public void test() {
        assertThat(parser.parse("2 * (3 + 5)").eval().getValue()).isEqualTo(2 * (3 + 5));

        assertThat(parser.parse("2 * (3 - 5)").eval().getValue()).isEqualTo(2 * (3 - 5));

    }
}
