package org.meeuw.math.operators;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.TextUtils;

/**
 * Like {@link java.util.function.BiPredicate}, but the difference is that this is not itself generic, but only its {@link #test(AlgebraicElement, AlgebraicElement)} method.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface AlgebraicComparisonOperator extends OperatorInterface {

    <E extends AlgebraicElement<E>>  boolean test(E arg1, E arg2);


    String stringify(String element1, String element2);

    default <E extends AlgebraicElement<E>> String stringify(E element1, E element2) {
        return stringify(element1.toString(), element2.toString());
    }

    default String getSymbol() {
        return stringify(TextUtils.PLACEHOLDER, TextUtils.PLACEHOLDER);
    }
}
