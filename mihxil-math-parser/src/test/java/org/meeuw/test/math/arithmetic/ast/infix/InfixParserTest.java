package org.meeuw.test.math.arithmetic.ast.infix;


import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.arithmetic.ast.Expression;
import org.meeuw.math.arithmetic.ast.infix.InfixParser;
import org.meeuw.math.arithmetic.ast.infix.ParseException;

public class InfixParserTest {
    Random random = new Random();

    @ParameterizedTest
    @ValueSource(strings = {
        "-1",
        "10.1",
        "sin(4)",
        "\"-1\""
    })
    public void terms(String string) throws ParseException {
        InfixParser<RealNumber> parser = new InfixParser<>(string, RealField.INSTANCE, this::getConstant);
        Expression<RealNumber> expression = parser.term();
        System.out.print("parsed: " + expression);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-1",
        "1 * (2 - 3)",
        "2 ⋅ 3",
        "e",
        "pi/2",
        "sin(pi/2)",
        "e ^ 0",
        "r4 + r5",
        "-pi"
    })
    public void parse(String string) throws ParseException {
        InfixParser<RealNumber> parser = new InfixParser<>(string, RealField.INSTANCE, this::getConstant);
        Expression<RealNumber> expression = parser.parse();
        System.out.println("parsed: " + expression + " -> " + expression.eval());

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-1",
        "1 * (2 - 3)",
        "2 ⋅ 3",
        "r4 + r5",
        "2 + 3!"
    })
    public void parseInteger(String string) throws ParseException {
        InfixParser<IntegerElement> parser = new InfixParser<>(string, Integers.INSTANCE, this::getIntConstant);
        Expression<IntegerElement> expression = parser.parse();
        System.out.println("parsed: " + expression + " -> " + expression.eval());

    }

    Optional<RealNumber> getConstant(String sign, String name) {
        RealNumber v = RealField.INSTANCE.getConstant(name)
                .orElse(RealField.INSTANCE.nextRandom(random));
        if ("-".equals(sign)) {
            v = v.negation();
        }
        return Optional.of(v);

    }

    Optional<IntegerElement> getIntConstant(String sign, String name) {
        IntegerElement v = Integers.INSTANCE.getConstant(name)
            .orElse(Integers.INSTANCE.nextRandom(random));
        if ("-".equals(sign)) {
            v = v.negation();
        }
        return Optional.of(v);

    }

}
