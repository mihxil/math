package org.meeuw.math.shapes;

import jakarta.validation.constraints.Min;

import org.meeuw.math.IntegerUtils;
import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

/**
 * Represents a rectangle defined by its width and height, both of which must be non-negative scalara.
 * This class provides methods to perform various geometric calculations such as rotation, area, perimeter,
 * diagonal length, and aspect ratio.
 *
 * @since 0.15
 */
public class Rectangle<F extends CompleteScalarFieldElement<F>> {

    private final F width;
    private final F height;

    /**
     *  @param width  the width of the rectangle, must be non-negative
     *  @param height the height of the rectangle, must be non-negative
     */
    public Rectangle(@Min(0) F width, @Min(0) F height) {
        this.width = width;
        this.height = height;
    }

    public F width() {
        return width;
    }

    public F height() {
        return height;
    }

    /**
     * Checks if the rectangle is vertical, meaning its width is smaller than its height.
     *
     * @return true if the rectangle is vertical, false otherwise
     */
    public boolean vertical() {
        return width.lt(height);
    }

    /**
     * Rotates the rectangle by a given angle in radians.
     * The new width and height are calculated based on the rotation transformation.
     *
     * @param angle the angle in radians to rotate the rectangle
     * @return a new Rectangle object with the rotated dimensions
     */
    public Rectangle<F> rotate(F angle) {

        F sin = angle.sin();
        F cos = angle.cos();
        return new Rectangle<>(
            width.times(cos).abs().plus(height.times(sin).abs()),

            width.times(sin).abs().plus(height.times(cos).abs()));

    }



    /**
     *
     * Calculates the area of the rectangle by multiplying its width and height.
     */
    public F area() {
        return  width.times(height);
    }

    /**
     * Calculates the perimeter of the rectangle by summing twice the width and twice the height.
     * The result is returned as a long to accommodate larger values.
     *
     * @return the perimeter of the rectangle
     */
    public F perimeter() {
        return width.times(2).plus(height.times(2));
    }
    /**
     * Calculates the diagonal length of the rectangle using the Pythagorean theorem.
     * The diagonal is computed as the square root of the sum of the squares of the width and height.
     *
     * @return the length of the diagonal
     */
    public F diagonal() {
        return (width.sqr().plus(height.sqr())).sqrt();
    }

    /**
     * Checks if the rectangle is a square, meaning its width and height are equal.
     *
     * @return true if the rectangle is a square, false otherwise
     */
    public boolean isSquare() {
        return width.eq(height);
    }

    /**
     * Returns the aspect ratio of the rectangle in the format "width:height".
     * The values are reduced to their simplest form using the greatest common divisor ({@link org.meeuw.math.IntegerUtils#gcd(int, int) GCD}).
     *
     * @return a string representing the aspect ratio
     */
    public String aspectRatio() {
        long gcd = IntegerUtils.gcd(width.longValue(), height.longValue());
        return String.format("%s:%s", width.dividedBy(gcd), height.dividedBy(gcd));
    }

    public String toString() {
        return "Rectangle{" + width() + "x" +  height() + '}';
    }
}
