package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.abstractalgebra.categoryofgroups.Element;
import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * A general group , with one operation, and a 'unity' element, for this operation.
 *
 * @see MultiplicativeGroup For a group where the operation is explicitely called 'multiplication'
 * @see AdditiveGroup       For a group where the operation is 'addition'.
 * @since 0.8
 */
public interface Group<E extends GroupElement<E>> extends Magma<E>, Element {

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(Magma.UNARY_OPERATORS, BasicAlgebraicUnaryOperator.INVERSION);

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    /**
     * @return The unity element, for which the {@link GroupElement#operate(MagmaElement)} returns just the other value.
     */
    E unity();



}
