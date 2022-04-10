package org.meeuw.math.abstractalgebra.trivial;

import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Example(Group.class)
public class TrivialGroup extends AbstractAlgebraicStructure<TrivialGroupElement>
    implements Group<TrivialGroupElement>, Streamable<TrivialGroupElement> {

    public  static final TrivialGroup INSTANCE = new TrivialGroup();

    private TrivialGroup() {
    }

    @Override
    public TrivialGroupElement unity() {
        return TrivialGroupElement.e;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ONE;
    }

    @Override
    public Stream<TrivialGroupElement> stream() {
        return Stream.of(TrivialGroupElement.e);
    }

    @Override
    public String toString() {
        return "C" + TextUtils.subscript(1);
    }

}
