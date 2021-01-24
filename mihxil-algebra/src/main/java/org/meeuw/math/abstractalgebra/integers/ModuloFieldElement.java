package org.meeuw.math.abstractalgebra.integers;

import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloFieldElement extends ModuloElement<ModuloFieldElement, ModuloField> implements FieldElement<ModuloFieldElement> {

    public ModuloFieldElement(int value, ModuloField structure) {
        super(value, structure);
    }

    @Override
    public ModuloFieldElement reciprocal() {
        // this is very crude:
        if (value == 0) {
            throw new DivisionByZeroException("reciprocal of 0");
        }
        OptionalInt first = IntStream
            .range(1, structure.divisor)
            .filter(a -> (a * value) % structure.divisor == 1)
            .findFirst();

        // use Extended Euclidean algorithms
        if (! first.isPresent()) {
            throw new ReciprocalException("No reciprocal found for " + value);
        }
        return new ModuloFieldElement(first.getAsInt(), structure);
    }

}
