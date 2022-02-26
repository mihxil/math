package org.meeuw.math.abstractalgebra.linear;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.Ring;
import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.text.TextUtils;


/**
 * As long as division is not required for inversion, a general linear group can also be defined on a {@link Ring}.
 *
 * This requirement boils down to the fact that the {@link AbstractInvertibleMatrix#determinant()} is plus or minus one.
 */
public class SpecialLinearGroup<E extends RingElement<E>> extends
    AbstractGeneralLinearGroup<
        SpecialLinearMatrix<E>,
        SpecialLinearGroup<E>,
        E,
        Ring<E>
        > implements Randomizable<SpecialLinearMatrix<E>> {


    private static final Map<Key, SpecialLinearGroup<?>> INSTANCES = new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    public static <E extends RingElement<E>> SpecialLinearGroup<E> of(final int length, final Ring<E> elementStructure) {
        final Key key = new Key(length, elementStructure);
        return (SpecialLinearGroup<E>) INSTANCES.computeIfAbsent(key, k -> new SpecialLinearGroup<E>(
            elementStructure,
            k.dimension
        ));
    }

    @Example(Ring.class)
    public static SpecialLinearGroup<IntegerElement> SL_N = SpecialLinearGroup.of(3, Integers.INSTANCE);


    protected SpecialLinearGroup(@NonNull Ring<E> elementStructure, int dimension) {
        super(elementStructure, dimension);
    }

    @Override
    SpecialLinearMatrix<E> of(E[][] elements) {
        SpecialLinearMatrix<E> result = new SpecialLinearMatrix<>(this, elements);

        return result;
    }

    @Override
    public SpecialLinearMatrix<E> newElement(E[][] matrix) throws InvalidElementCreationException {
        SpecialLinearMatrix<E> m = super.newElement(matrix);
        E det = m.determinant();
        if (! (det.eq(elementStructure.one()) || det.eq(elementStructure.one().negation()))) {
            throw new InvalidElementCreationException("The matrix " + m + " is not invertible, because det=" + det + " !in (1,-1)");
        }
        return m;
    }


    @Override
    public String toString() {
        return "SL" + TextUtils.subscript(dimension) + "(" + elementStructure.toString() + ")";
    }

    @Override
    public Stream<SpecialLinearMatrix<E>> stream() {
        return super.stream();
        //throw new NotStreamable("TODO");
    }


    @Override
    public SpecialLinearMatrix<E> nextRandom(Random random) {
        E[][] matrix = ArrayUtils.newSquareMatrix(elementStructure.getElementClass(), dimension);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == j) {
                    matrix[i][j] = elementStructure.one();
                    if (random.nextBoolean()) {
                        matrix[i][j]  = matrix[i][j].negation();
                    }
                } else if (i < j) {
                    matrix[i][j] = elementStructure.zero();
                } else {
                    matrix[i][j] = ((Randomizable<E>) elementStructure).nextRandom(random);
                }
            }
        }
        ArrayUtils.shuffle(random, matrix);

        return of(matrix);
    }
}

