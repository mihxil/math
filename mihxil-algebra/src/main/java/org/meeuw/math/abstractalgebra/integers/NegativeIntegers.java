package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.ADDITION;
import static org.meeuw.math.abstractalgebra.integers.NegativeInteger.MINUS_ONE;

/**
 * The 'Semigroup'  of  negative numbers
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Example(AdditiveAbelianSemiGroup.class)
public class NegativeIntegers
    extends AbstractIntegers<NegativeInteger>
    implements
    AdditiveAbelianSemiGroup<NegativeInteger> {

    private static final NavigableSet<Operator> OPERATORS = navigableSet(ADDITION);

    public static final NegativeIntegers INSTANCE = new NegativeIntegers();

    protected NegativeIntegers() {
        super(NegativeInteger.class);
    }

    @Override
    public NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    public Stream<NegativeInteger> stream() {
        return  Stream.iterate(MINUS_ONE, i -> i.plus(MINUS_ONE));
    }

    @Override
    public String toString() {
        return "ℕ" + TextUtils.superscript("-");
    }
}
