package org.meeuw.math.shapes.dim3;

import jakarta.validation.constraints.Min;
import lombok.Getter;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;

import static org.meeuw.math.uncertainnumbers.UncertainUtils.strictlyEqual;

/**
 * Represents a rectangle defined by its width and height, both of which must be non-negative scalara.
 * This class provides methods to perform various geometric calculations such as rotation, area, perimeter,
 * diagonal length, and aspect ratio.
 *
 * @since 0.15
 */
@Getter
public class RectangularCuboid<F extends ScalarFieldElement<F, C>, C extends CompleteScalarFieldElement<C>> implements Polyhedron<F, C, RectangularCuboid<F, C>> {

    private final F width;
    private final F height;
    private final F depth;

    /**
     *  @param width  the width of the rectangle, must be non-negative
     *  @param height the height of the rectangle, must be non-negative
     */
    public RectangularCuboid(@Min(0) F width, @Min(0) F height, @Min(0) F depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public static RectangularCuboid<RationalNumber, BigDecimalElement> of(int i, int i1, int i2) {
        return new RectangularCuboid<>(RationalNumber.of(i), RationalNumber.of(i1), RationalNumber.of(i2));
    }

    public F width() {
        return width;
    }

    public F height() {
        return height;
    }

    public F depth() {
        return depth;
    }


    @Override
    public C volume() {
        return exactVolume().complete();
    }

    public F exactVolume() {
        return  width.times(height).times(depth);
    }

    @Override
    public C surfaceArea() {
        return exactSurfaceArea().complete();
    }

    public F exactSurfaceArea() {
        return width.times(height).times(2)
            .plus(width.times(depth).times(2))
            .plus(height.times(depth).times(2))
            ;
    }

    @Override
    public int vertices() {
        return 8;
    }

    @Override
    public int edges() {
        return 12;
    }

    @Override
    public int faces() {
        return 6;
    }


    @SuppressWarnings("unchecked")
    @Override
    public  RectangularCuboid<C, C> complete() {
        return new RectangularCuboid<>(width.complete(), height.complete(), depth.complete());
    }

    @Override
    public ScalarField<F, C> field() {
        return width.getStructure();
    }

    @Override
    public boolean eq(RectangularCuboid<F, C> other) {
        return width.eq(other.width) && height.eq(other.height) && depth.eq(other.depth);
    }
    @Override
    public boolean strictlyEquals(Object o) {
        return strictlyEqual(this, o, RectangularCuboid::width, RectangularCuboid::height, RectangularCuboid::depth);
    }

    @Override
    public RectangularCuboid<F, C> times(F multiplier) {
        return new RectangularCuboid<>(width.times(multiplier), height.times(multiplier), depth.times(multiplier));
    }

    @Override
    public RectangularCuboid<F, C> times(int multiplier) {
        return new RectangularCuboid<>(width.times(multiplier), height.times(multiplier), depth.times(multiplier));
    }

    @Override
    public RectangularCuboid<F, C> times(double multiplier) {
        return new RectangularCuboid<>(width.times(multiplier), height.times(multiplier), depth.times(multiplier));
    }


    public String toString() {
        return "RectangularCuboid{" + width() + "x" +  height() + "x" + depth() + '}';
    }


}
