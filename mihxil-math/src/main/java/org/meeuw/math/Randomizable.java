package org.meeuw.math;

import java.util.Random;

public interface Randomizable<E> {

    E nextRandom(Random random);
}
