package org.meeuw.physics;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import org.checkerframework.checker.nullness.qual.NonNull;
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
        return new UnitsImpl(siFactor, unitExponents);
    }


     default List<BaseUnit> getBaseUnits() {
        Class<?> thisClass = this.getClass();

        List<BaseUnit> result = new ArrayList<>();
        for (Class<?> subclass : thisClass.getDeclaredClasses()) {
            if (BaseUnit.class.isAssignableFrom(subclass)) {
                if (Enum.class.isAssignableFrom(subclass)) {
                    for (Object u : subclass.getEnumConstants()) {
                        result.add((BaseUnit) u);
                    }
                }
            }

        }
        return Collections.unmodifiableList(result);
    }

    default List<Units> getUnits()  {
        final Class<?> thisClass = this.getClass();
        final List<Units> result = new ArrayList<>(getBaseUnits());
        for (Field f : thisClass.getDeclaredFields()) {
            if (Units.class.isAssignableFrom(f.getType())) {
                if (Modifier.isStatic(f.getModifiers())) {
                    try {
                        result.add((Units) f.get(null));
                    } catch (IllegalAccessException e) {
                        // ignore
                    }
                }
            }
        }
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
