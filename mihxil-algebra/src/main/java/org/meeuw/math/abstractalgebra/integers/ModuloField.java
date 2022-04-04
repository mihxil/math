package org.meeuw.math.abstractalgebra.integers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.Example;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.validation.Prime;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(value = Field.class, string = "ℤ/nℤ")
public class ModuloField extends ModuloStructure<ModuloFieldElement, ModuloField>
    implements Field<ModuloFieldElement> {

    private static final Map<Integer, ModuloField> INSTANCES = new ConcurrentHashMap<>();

    public static ModuloField of(@Prime int divisor) throws InvalidElementCreationException {
        return INSTANCES.computeIfAbsent(divisor, ModuloField::new);
    }

    @Example(Field.class)
    public static final ModuloField Z3Z = of(3);

    private ModuloField(int divisor) throws InvalidElementCreationException {
        super(ModuloFieldElement.class, divisor);
        if (! Utils.isPrime(divisor)) {
            throw new InvalidElementCreationException("" + divisor + " is not a prime");
        }
    }

    @Override
    public ModuloFieldElement element(int v) {
        return new ModuloFieldElement(v, this);
    }

    @Override
    public String toString() {
        return "ℤ/" + divisor + "ℤ";
    }

}
