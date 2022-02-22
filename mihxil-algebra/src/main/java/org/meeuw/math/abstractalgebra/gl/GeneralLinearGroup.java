package org.meeuw.math.abstractalgebra.gl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.text.TextUtils;

public class GeneralLinearGroup<E extends FieldElement<E>>
    extends AbstractAlgebraicStructure<InvertibleMatrix<E>>
    implements MultiplicativeGroup<InvertibleMatrix<E>>,
    Streamable<InvertibleMatrix<E>>
{

    @Getter
    private final int dimension;

    @Getter
    private final Field<E> elementField;

    private final InvertibleMatrix<E> one;

    private static final Map<Key, GeneralLinearGroup<?>> INSTANCES = new ConcurrentHashMap<>();


    private GeneralLinearGroup(
        Class<InvertibleMatrix<E>> clazz,
        Field<E> field,
        int dimension) {
        super(clazz);
        this.dimension = dimension;
        this.elementField = field;
        this.one = new InvertibleMatrix<>(this, one(elementField, dimension));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<InvertibleMatrix<E>> stream() {
        if (elementField.getCardinality().compareTo(Cardinality.ALEPH_0) <=0) {
            Class<E> elementClass = elementField.getElementClass();
            return StreamUtils.cartesianStream(() -> ((Streamable<E>) elementField).stream(), dimension * dimension)
                .map(es -> {
                    try {
                        return InvertibleMatrix.of(elementClass, es);
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

    @SuppressWarnings("unchecked")
    static <E extends FieldElement<E>> GeneralLinearGroup<E> of(InvertibleMatrix<E> orthogonalMatrix){
        E[] [] matrix = orthogonalMatrix.matrix;
        final Key key = new Key(matrix.length, matrix[0][0].getStructure());
        return (GeneralLinearGroup<E>) INSTANCES.computeIfAbsent(key, k -> new GeneralLinearGroup<E>(
            (Class<InvertibleMatrix<E>>) orthogonalMatrix.getClass(),
            (Field<E>) k.field,
            k.dimension
        ));

    }

    @Override
    public boolean multiplicationIsCommutative() {
        return dimension < 2;
    }

    @EqualsAndHashCode
    public static class Key {
        final int dimension;
        final Field<?> field;

        public Key(int dimension, Field<?> field) {
            this.dimension = dimension;
            this.field = field;
        }
    }

    @Override
    public Cardinality getCardinality() {
        return elementField.getCardinality();
    }

    @Override
    public InvertibleMatrix<E> one() {
        return one;
    }

    @Override
    public String toString() {
        return "GL" + TextUtils.subscript(dimension) + "(" + elementField.toString() + ")";
    }

    @SuppressWarnings("unchecked")
    protected static <E extends FieldElement<E>> E[][] one(Field<E> scalarField, int dimension) {
        E[][] values = (E[][]) Array.newInstance(scalarField.getElementClass(), dimension, dimension);
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++)
                if (i == j) {
                    values[i][j] = scalarField.one();
                } else {
                    values[i][j] = scalarField.zero();
                }
        }
        return values;
    }
}
