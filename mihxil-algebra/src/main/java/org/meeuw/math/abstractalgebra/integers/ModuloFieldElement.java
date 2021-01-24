package org.meeuw.math.abstractalgebra.integers;

import java.util.stream.IntStream;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloFieldElement extends ModuloElement<ModuloFieldElement, ModuloField> implements FieldElement<ModuloFieldElement> {

    ModuloFieldElement(int value, ModuloField structure) {
        super(value, structure);
    }

    @Override
    public ModuloFieldElement reciprocal() {
        // this is very crude:
        if (value == 0) {
            throw new DivisionByZeroException("reciprocal of 0");
        }
        int first = IntStream
            .range(1, structure.divisor)
            .filter(a -> (a * value) % structure.divisor == 1)
            .findFirst()
            .orElseThrow(() -> new ReciprocalException("No reciprocal found for " + value));

        // use Extended Euclidean algorithms

        return new ModuloFieldElement(first, structure);
    }

}
