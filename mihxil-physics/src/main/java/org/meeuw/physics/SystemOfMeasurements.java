package org.meeuw.physics;

import java.util.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.ReflectionUtils;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.math.uncertainnumbers.field.UncertainRealField;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public interface SystemOfMeasurements {

    @NonNull
    Unit forDimension(Dimension dimension);

    /**
     * Returns in this system of measurements the preferred units for the given dimensional analysis
     */
    @NonNull
    default Units forDimensions(DimensionalAnalysis dimensionalAnalysis) {
        UnitExponent[] unitExponents = dimensionalAnalysis.stream()
            .map(dimensionExponent -> dimensionExponent.toUnitExponent(this))
            .toArray(UnitExponent[]::new);
        final UncertainReal siFactor = Arrays.stream(unitExponents)
            .map(UnitExponent::getSIFactor)
            .reduce(UncertainRealField.INSTANCE.one(), UncertainReal::times);
        return new CompositeUnits(siFactor, unitExponents);
    }


    default List<BaseUnit> getBaseUnits() {
        List<BaseUnit> result = new ArrayList<>();
        ReflectionUtils.forConstants(this.getClass(), BaseUnit.class, result::add);
        return Collections.unmodifiableList(result);
    }

    default List<Units> getUnits()  {
        final List<Units> result = new ArrayList<>(getBaseUnits());
        ReflectionUtils.forConstants(this.getClass(), Units.class, result::add);
        return Collections.unmodifiableList(result);
    }

    default Units forQuantity(Quantity quantity) {
        for (Units unit : getUnits()) {
            if (unit.getQuantities().contains(quantity)) {
                return unit;
            }
        }
        return forDimensions(quantity.getDimensionalAnalysis());
    }

    default Units forDimensions(DimensionExponent... dimensions) {
        return forDimensions(DimensionalAnalysis.of(dimensions));
    }
}
