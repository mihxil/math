package org.meeuw.math.exceptions;

/**
 * Thrown if trying to perform an operation on an element, that is impossible because some underlying implementation is not complete. E.g. for most operations on {@link org.meeuw.math.abstractalgebra.Vector} it is not essential that the elements of the vector are complete, but implementation may also implement e.g.  {@link org.meeuw.math.numbers.Sizeable}, requiring that you can take the square root, which is only possible if the element are {@link org.meeuw.math.abstractalgebra.CompleteFieldElement}.
 * @author Michiel Meeuwissen
 */
public class FieldInCompleteException extends UnsupportedMathOperationException {
    public FieldInCompleteException(String s) {
        super(s);
    }
}
