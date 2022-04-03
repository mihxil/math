package org.meeuw.math.abstractalgebra.klein;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;

/**
 * The structure of the {@link org.meeuw.math.abstractalgebra.klein} 4 group
 */
public class KleinGroup implements Group<KleinElement>, Streamable<KleinElement> {

    @Example(Group.class)
    public static final KleinGroup INSTANCE = new KleinGroup();

    private KleinGroup() {
    }
    @Override
    public Cardinality getCardinality() {
        return new Cardinality(KleinElement.values().length);
    }

    @Override
    public Class<KleinElement> getElementClass() {
        return KleinElement.class;
    }

    @Override
    public KleinElement unity() {
        return KleinElement.e;
    }

    @Override
    public Stream<KleinElement> stream() {
        return Arrays.stream(KleinElement.values());
    }

    @Override
    public KleinElement nextRandom(Random random) {
        return KleinElement.values()[random.nextInt(KleinElement.values().length)];
    }

    @Override
    public String toString() {
        return "V";
    }
}
