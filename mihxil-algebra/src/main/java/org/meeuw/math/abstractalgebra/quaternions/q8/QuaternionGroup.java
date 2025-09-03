package org.meeuw.math.abstractalgebra.quaternions.q8;

import java.util.*;
import java.util.stream.Stream;

import org.meeuw.math.Singleton;
import org.meeuw.math.Synonym;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.text.TextUtils;

@Singleton
public class QuaternionGroup implements Group<QuaternionElement>, Streamable<QuaternionElement> {
    public static final QuaternionGroup INSTANCE = new QuaternionGroup();

    private QuaternionGroup() {

    }

    @Override
    @Synonym("e")
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
    public QuaternionElement nextRandom(Random random) {
        return QuaternionElement.values()[random.nextInt(QuaternionElement.values().length)];
    }

    @Override
    public String toString() {
        return "Q" + TextUtils.subscript(8);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("The quaternion group is a non-abelian group of order eight, isomorphic to the eight-element subset {1,i,j,k,-1,-i,-j,-k} of the quaternions under multiplication.");
    }

    @Override
    public QuaternionElement fromString(String q) {
        for (QuaternionElement e : QuaternionElement.values()) {
            if (e.name().equals(q)) {
                return e;
            }
            if (e.string.equals(q)) {
                return e;
            }
        }
        throw new NotParsable(q);
    }


}
