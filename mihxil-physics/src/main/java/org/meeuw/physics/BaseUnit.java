package org.meeuw.physics;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Marker interface for a 'base' unit of the associated {@link SystemOfMeasurements}.
 *
 * It is a singular {@link Unit} and can be implemented as enums, because a {@link SystemOfMeasurements} normally has
 * a fixed number of {@link BaseUnit}s, one for each possible {@link Dimension}.
 */
public interface BaseUnit extends Unit {

    SystemOfMeasurements getSystem();

    Dimension getDimension();

    @Override
    default List<Quantity> getQuantities() {
        DimensionalAnalysis analysis = new DimensionalAnalysis(DimensionExponent.of(getDimension(), 1));
        return Quantity.getQuantities().stream()
            .filter(q -> q.getDimensionalAnalysis().equals(analysis))
            .collect(Collectors.toList());
    }

    @Override
    default DimensionalAnalysis getDimensions() {
        return DimensionalAnalysis.of(getDimension());
    }

    @Override
    default Units times(Units multiplier) {
        return Units.of(this).times(multiplier);
    }

    @Override
    default Units reciprocal() {
        return Units.of(this).reciprocal();
    }
}
