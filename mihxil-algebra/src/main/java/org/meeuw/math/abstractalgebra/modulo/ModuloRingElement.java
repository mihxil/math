package org.meeuw.math.abstractalgebra.modulo;

import lombok.EqualsAndHashCode;

import org.meeuw.math.abstractalgebra.Ring;
import org.meeuw.math.abstractalgebra.RingElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class ModuloRingElement implements RingElement<ModuloRingElement> {

    private final int value;
    private final ModuloRing structure;

    public ModuloRingElement(int value, ModuloRing structure) {
        this.value = value % structure.divisor;
        this.structure = structure;
    }

    @Override
    public Ring<ModuloRingElement> getStructure() {
        return structure;
    }

    @Override
    public ModuloRingElement times(ModuloRingElement multiplier) {
        return new ModuloRingElement(value * multiplier.value % structure.divisor, structure);
    }

    @Override
    public ModuloRingElement plus(ModuloRingElement summand) {
        return new ModuloRingElement((value + summand.value) % structure.divisor, structure);
    }

    @Override
    public ModuloRingElement negation() {
        return new ModuloRingElement((-1 * value ) % structure.divisor, structure);
    }

    @Override
    public String toString() {
        return String.valueOf(value); /* + "%" + structure.divisor;*/
    }


}
