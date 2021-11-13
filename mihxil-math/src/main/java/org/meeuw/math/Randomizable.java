package org.meeuw.math;

import java.util.Random;

/**
 * @since 0.7
 */
public interface Randomizable<E> {

    E nextRandom(Random random);
}
