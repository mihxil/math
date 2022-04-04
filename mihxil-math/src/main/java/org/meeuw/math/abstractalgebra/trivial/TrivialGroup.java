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
public class TrivialGroup extends AbstractAlgebraicStructure<TrivialElement>
    implements Group<TrivialElement>, Streamable<TrivialElement> {

    public  static final TrivialGroup INSTANCE = new TrivialGroup();

    private TrivialGroup() {
    }

    @Override
    public TrivialElement unity() {
        return TrivialElement.e;
    }

    @Override
    public Cardinality getCardinality() {
        return new Cardinality(1);
    }

    @Override
    public Stream<TrivialElement> stream() {
        return Stream.of(TrivialElement.e);
    }

    @Override
    public String toString() {
        return "C" + TextUtils.subscript(1);
    }

}
