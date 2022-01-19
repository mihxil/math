package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.exceptions.DivisionByZeroException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloFieldElement
    extends ModuloElement<ModuloFieldElement, ModuloField>
    implements FieldElement<ModuloFieldElement> {

    ModuloFieldElement(int value, ModuloField structure) {
        super(value, structure);
    }

    @Override
    public ModuloFieldElement reciprocal() {
        if (value == 0) {
            throw new DivisionByZeroException("reciprocal of 0");
        }
        // https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
        int t = 0;
        int newt = 1;
        int r = getStructure().divisor;
        int newr = value;
        while (newr != 0) {
            int quotient = r / newr;
            int oldt = newt;
            newt = t - quotient * newt;
            t = oldt;
            int oldr = newr;
            newr = r - quotient * newr;
            r = oldr;
        }

        assert r <= 1; // the divisor is prime, so this should always have been possible

        if (t < 0) {
            t = t + getStructure().divisor;
        }

        return new ModuloFieldElement(t, structure);
    }

    @Override
    public ModuloFieldElement dividedBy(long divisor) {
        return times(new ModuloFieldElement((int) divisor % structure.divisor, structure).reciprocal());
    }

    @Override
    public ModuloFieldElement times(long multiplier) {
        return new ModuloFieldElement((int) ((long) value * multiplier) % getStructure().divisor, structure);
    }
}
