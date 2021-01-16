package org.meeuw.math.abstractalgebra.integers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.exceptions.InvalidElementCreationException;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ModuloField extends ModuloStructure<ModuloFieldElement, ModuloField> implements Field<ModuloFieldElement> {

    private static final Map<Integer, ModuloField> INSTANCES = new ConcurrentHashMap<>();

    public static ModuloField of(int divisor) throws InvalidElementCreationException {
        return INSTANCES.computeIfAbsent(divisor, ModuloField::new);
    }

    private ModuloField(int divisor) throws InvalidElementCreationException {
        super(ModuloFieldElement.class, divisor);
        if (! Utils.isPrime(divisor)) {
            throw new InvalidElementCreationException("" + divisor + " is not prime");
        }
    }

    @Override
    ModuloFieldElement element(int v) {
        return new ModuloFieldElement(v, this);
    }

}
