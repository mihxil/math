package org.meeuw.math.abstractalgebra.gl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.MatrixUtils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.text.TextUtils;

public abstract class AbstractGeneralLinearGroup<
    M extends AbstractInvertibleMatrix<M, MS, E, ES>,
    MS extends AbstractGeneralLinearGroup<M, MS, E, ES>,
    E extends RingElement<E>,
    ES extends Ring<E>
    >
    extends AbstractAlgebraicStructure<M>
    implements MultiplicativeGroup<M>,
    Streamable<M>
{

    @Getter
    private final int dimension;

    @Getter
    private final ES elementStructure;

    private final M one;


    protected AbstractGeneralLinearGroup(
        @NonNull ES elementStructure,
        int dimension) {
        super();
        this.dimension = dimension;
        this.elementStructure = elementStructure;
        this.one = of(one(elementStructure, dimension));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<M> stream() {
        if (elementStructure.getCardinality().compareTo(Cardinality.ALEPH_0) <=0) {
            return StreamUtils.cartesianStream(() -> ((Streamable<E>) elementStructure).stream(), dimension * dimension)
                .map(es -> {
                    try {
                        return of(es);
                    } catch (InvalidElementCreationException ive) {
                        return null;
                    }
                    }
                )
                .filter(Objects::nonNull);
        } else {
            throw new NotStreamable();
        }
    }

    M of(E[] elements) {
        return of(MatrixUtils.squareMatrix(elementStructure.getElementClass(), elements));
    }

    abstract M of(E[][] elements);


    @SuppressWarnings("unchecked")
    public  M newMatrix(E... matrix) {
        return of(MatrixUtils.squareMatrix(elementStructure.getElementClass(), matrix));
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return dimension < 2;
    }

    @EqualsAndHashCode
    public static class Key {
        final int dimension;
        final Ring<?> field;

        public Key(int dimension, Ring<?> field) {
            this.dimension = dimension;
            this.field = field;
        }
    }

    @Override
    public Cardinality getCardinality() {
        return elementStructure.getCardinality();
    }

    @Override
    public M one() {
        return one;
    }

    @Override
    public String toString() {
        return "GL" + TextUtils.subscript(dimension) + "(" + elementStructure.toString() + ")";
    }

    protected static <E extends RingElement<E>> E[][] one(Ring<E> elementStructure, int dimension) {
        E[][] values = elementStructure.newMatrix(dimension, dimension);
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++)
                if (i == j) {
                    values[i][j] = elementStructure.one();
                } else {
                    values[i][j] = elementStructure.zero();
                }
        }
        return values;
    }
}
