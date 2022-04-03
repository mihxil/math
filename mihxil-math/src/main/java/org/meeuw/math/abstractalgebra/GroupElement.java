package org.meeuw.math.abstractalgebra;

/**
 * A group where it is not defined whether the operation is addition or multiplication.
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface GroupElement<E extends GroupElement<E>>
    extends MagmaElement<E> {

    @Override
    Group<E> getStructure();

    /**
     * @return The inverse element for {@link #operate(MagmaElement)}, such that {@code self.operate(self.inverse())} = {@link Group#unity()}
     */
    E inverse();


}
