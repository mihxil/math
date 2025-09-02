package org.meeuw.math.abstractalgebra.quaternions;

import java.util.Arrays;
import java.util.stream.Stream;

import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

@Singleton
public class QuaternionGroup implements Group<QuaternionElement>, Streamable<QuaternionElement> {
    public static final QuaternionGroup INSTANCE = new QuaternionGroup();

    private QuaternionGroup() {

    }

    @Override
    public QuaternionElement unity() {
        return QuaternionElement.e;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.of(QuaternionElement.values().length);
    }

    @Override
    public Class<QuaternionElement> getElementClass() {
        return QuaternionElement.class;
    }

    @Override
    public Stream<QuaternionElement> stream() {
        return Arrays.stream(QuaternionElement.values());
    }

    @Override
    public String toString() {
        return "Q" + TextUtils.subscript(8);
    }


}
