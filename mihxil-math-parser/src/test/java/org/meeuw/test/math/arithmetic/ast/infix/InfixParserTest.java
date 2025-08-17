package org.meeuw.test.math.arithmetic.ast.infix;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.arithmetic.ast.Expression;
import org.meeuw.math.arithmetic.ast.infix.InfixParser;
import org.meeuw.math.arithmetic.ast.infix.ParseException;

public class InfixParserTest {


    @ParameterizedTest
    @ValueSource(strings = {
        "-1",
        "10.1",
        "sin(4)",
        "\"-1\""
    })
    public void terms(String string) throws ParseException {
        InfixParser<RealNumber> parser = new InfixParser<>(string, RealField.INSTANCE);
        Expression<RealNumber> expression = parser.term();
        System.out.print("parsed: " + expression);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-1",
        "1 * (2 - 3)",
        "e",
        "pi/2",
        "sin(pi/2)",
        "e ^ 0"

    })
    public void parse(String string) throws ParseException {
        InfixParser<RealNumber> parser = new InfixParser<>(string, RealField.INSTANCE);

        Expression<RealNumber> expression = parser.parse();
        System.out.print("parsed: " + expression + " -> " + expression.eval());

    }

}
