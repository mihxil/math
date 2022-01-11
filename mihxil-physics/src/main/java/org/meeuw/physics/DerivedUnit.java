package org.meeuw.physics;

import lombok.*;

import java.util.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.math.uncertainnumbers.field.UncertainRealField;

/**
 * A 'derived' unit is a singular {@link Unit} which is not a {@link BaseUnit}. It has one symbol, and
 * may itself be wrapped in a {@link DimensionExponent}.
 *
 * Its {@link DimensionalAnalysis} may be complex though. E.g. a {@link SI#J} is a derived unit.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class DerivedUnit implements Unit {

    @Getter
    @NonNull
    final UncertainReal SIFactor;

    final int[] exponents;

    @Getter
    @With
    final String name;

    @Getter
    @With
    final String description;

    @With
    final List<Quantity> quantities;

    public DerivedUnit(
        @NonNull UncertainReal SIFactor,
        String name,
        String description,
        UnitExponent... siExponents) {
        this(SIFactor, null, name, description, Arrays.asList(siExponents), null);
    }


    /**
     * Used by {@link With}ers. Not unused as reported by Intellij.
     */
    private DerivedUnit(
        UncertainReal SIFactor,
        int[] exponents,
        String name,
        String description,
        List<Quantity> quantities) {
        this(SIFactor, exponents, name, description, null, quantities);
    }


    @lombok.Builder
    private DerivedUnit(
        @NonNull UncertainReal siFactor,
        final int[] exponents,
        final String name,
        final String description,
        final List<UnitExponent> unitExponents,
        final @Singular @Nullable List<Quantity> quantities
        ) {
        this.exponents =  new int[SIUnit.values().length];
        if (exponents != null) {
            System.arraycopy(exponents, 0, this.exponents, 0, this.exponents.length);
        }
        if (unitExponents != null) {
            for (UnitExponent f : unitExponents) {
                siFactor = siFactor.times(f.getSIFactor());
                int[] dimensions = f.getDimensions().getExponents();
                for (int d = 0; d < dimensions.length; d++) {
                    this.exponents[d] += dimensions[d];
                }
            }
        }
        this.name = name;
        this.description = description;
        this.SIFactor = siFactor;
        this.quantities = quantities == null ? Collections.emptyList() : quantities;
    }

    public DerivedUnit(Units units, String name, String description) {
        this.exponents = new int[SIUnit.values().length];
        System.arraycopy(units.getDimensions().getExponents(), 0, this.exponents, 0, this.exponents.length);
        this.name = name;
        this.description = description;
        this.SIFactor = units.getSIFactor();
        this.quantities = Collections.emptyList();
    }


    public DerivedUnit(String name, String description, UncertainReal siFactor, Units derivedUnit) {
        this.exponents = new int[SIUnit.values().length];
        System.arraycopy(derivedUnit.getDimensions().exponents, 0, this.exponents, 0, this.exponents.length);
        this.name = name;
        this.description = description;
        this.SIFactor = derivedUnit.getSIFactor().times(siFactor);
        this.quantities = Collections.emptyList();
    }

    public DerivedUnit(String name, String description, @NonNull UncertainReal siFactor, SIUnit siUnit) {
        this.exponents = new int[SIUnit.values().length];
        this.exponents[siUnit.ordinal()] = 1;
        this.name = name;
        this.description = description;
        this.SIFactor = siFactor;
        this.quantities = Collections.emptyList();
    }

    public DerivedUnit(String name, String description, UnitExponent... siExponents) {
        this(UncertainRealField.INSTANCE.one(), name, description,  siExponents);
    }

    @Override
    public DimensionalAnalysis getDimensions() {
        return new DimensionalAnalysis(this.exponents);
    }

    @Override
    public String name() {
        return name;
    }

    public PhysicalConstant asSIConstant() {
        return new PhysicalConstant(
            name,
            SIFactor,
            SIUnit.toUnits(exponents),
            description
        );
    }

    @Override
    public List<Quantity> getQuantities() {
        return Collections.unmodifiableList(quantities);
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public Units times(Units multiplier) {
        if (multiplier.isOne()){
            return this;
        }
        int[] exponents = getDimensions().times(multiplier.getDimensions()).getExponents();

        UncertainReal factor = SIFactor.times(multiplier.getSIFactor());

        if (Arrays.stream(exponents).allMatch(i -> i == 0) && factor.isOne()) {
            return DerivedUnit.DIMENSIONLESS;
        }

        return DerivedUnit.builder()
            .siFactor(factor)
            .name(name + TextUtils.TIMES + multiplier)
            .exponents(exponents)
            .build();
    }

    @Override
    public Units reciprocal() {
        int[] exponents = new int[this.exponents.length];
        for (int i = 0; i < this.exponents.length; i++) {
            exponents[i] = -1 * this.exponents[i];
        }
        UncertainReal reciprocalFactor = SIFactor.reciprocal();
        if (Arrays.stream(exponents).allMatch(i -> i == 0) && reciprocalFactor.isOne()) {
            return DerivedUnit.DIMENSIONLESS;
        }
        return DerivedUnit.builder()
            .siFactor(reciprocalFactor)
            .name("(" + name + ")" + TextUtils.superscript(-1))
            .exponents(exponents)
            .build();
    }

    @Override
    public DerivedUnit withQuantity(Quantity... quantity) {
        List<Quantity> result = new ArrayList<>(quantities);
        result.addAll(Arrays.asList(quantity));
        return withQuantities(result);
    }


    @SuppressWarnings({"EqualsDoesntCheckParameterClass", "com.haulmont.jpb.EqualsDoesntCheckParameterClass"})
    @Override
    public boolean equals(Object o) {
        return Units.equals(this, o);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Builder {
        public Builder unitExponent(UnitExponent... siExponents) {
            return unitExponents(Arrays.asList(siExponents));
        }


    }
}
