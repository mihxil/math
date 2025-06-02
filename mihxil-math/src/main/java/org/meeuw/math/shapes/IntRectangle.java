package org.meeuw.math.shapes;

import jakarta.validation.constraints.Min;

import static org.meeuw.math.IntegerUtils.gcd;

public record IntRectangle(
    @Min(0) int width,
    @Min(0) int height)  {

    /**
     * Checks if the rectangle is vertical, meaning its width is smaller than its height.
     *
     * @return true if the rectangle is vertical, false otherwise
     */
    boolean vertical() {
        return width < height;
    }

    /**
     * Rotates the rectangle by a given angle in radians.
     * The new width and height are calculated based on the rotation transformation.
     *
     * @param angle the angle in radians to rotate the rectangle
     * @return a new Rectangle object with the rotated dimensions
     */
    IntRectangle rotate(double angle) {

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return new IntRectangle(
            (int) Math.round(Math.abs(width * cos) + Math.abs(height * sin)),
            (int) Math.round(Math.abs(width * sin) + Math.abs(height * cos))
        );
    }



    /**
     * A convenience method to {@link #rotate(double)} the rectangle by a given angle in degrees (using {@link Math#toRadians(double)}).
     */
    IntRectangle rotateDegrees(Double angle) {
        if (angle == null) {
            return this;
        }
        return rotate(Math.toRadians(angle));
    }

    /**
     *
     * Calculates the area of the rectangle by multiplying its width and height.
     */
    long area() {
        return (long) width * height;
    }

    /**
     * Calculates the perimeter of the rectangle by summing twice the width and twice the height.
     * The result is returned as a long to accommodate larger values.
     *
     * @return the perimeter of the rectangle
     */
    long perimeter() {
        return (long) 2 * width + (long) 2 * height;
    }
    /**
     * Calculates the diagonal length of the rectangle using the Pythagorean theorem.
     * The diagonal is computed as the square root of the sum of the squares of the width and height.
     *
     * @return the length of the diagonal
     */
    double diagonal() {
        return Math.sqrt(width * width + height * height);
    }

    /**
     * Checks if the rectangle is a square, meaning its width and height are equal.
     *
     * @return true if the rectangle is a square, false otherwise
     */
    boolean isSquare() {
        return width == height;
    }

    /**
     * Returns the aspect ratio of the rectangle in the format "width:height".
     * The values are reduced to their simplest form using the greatest common divisor ({@link org.meeuw.math.IntegerUtils#gcd(int, int) GCD}).
     *
     * @return a string representing the aspect ratio
     */
    public String aspectRatio() {
        int gcd = gcd(width, height);
        return String.format("%d:%d", width / gcd, height / gcd);
    }
}
