package org.meeuw.math.shapes.dim3;

import jakarta.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

/**
 * Represents a rectangle defined by its width and height, both of which must be non-negative scalara.
 * This class provides methods to perform various geometric calculations such as rotation, area, perimeter,
 * diagonal length, and aspect ratio.
 *
 * @since 0.15
 */
public class RectangularCuboid<F extends CompleteScalarFieldElement<F>> implements Volume<F, RectangularCuboid<F>> {

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
    public F volume() {
        return  width.times(height).times(depth);
    }

    @Override
    public F surfaceArea() {
        return width.times(height).times(2)
            .plus(width.times(depth).times(2))
            .plus(height.times(depth).times(2));
    }

    @Override
    public boolean eq(RectangularCuboid<F> other) {
        return false;
    }


    public String toString() {
        return "RectangularCuboid{" + width() + "x" +  height() + "x" + depth() + '}';
    }
}
