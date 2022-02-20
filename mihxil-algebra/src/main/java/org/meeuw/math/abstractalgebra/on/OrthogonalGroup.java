package org.meeuw.math.abstractalgebra.on;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;

public class OrthogonalGroup<E extends FieldElement<E>>
    extends AbstractAlgebraicStructure<OrthogonalMatrix<E>>
    implements MultiplicativeGroup<OrthogonalMatrix<E>> {

    @Getter
    private final int dimension;

    @Getter
    private final Field<E> elementField;

    private final OrthogonalMatrix<E> one;

    private static final Map<Key, OrthogonalGroup<?>> INSTANCES = new ConcurrentHashMap<>();


    private OrthogonalGroup(
        Class<OrthogonalMatrix<E>> clazz,
        Field<E> field,
        int dimension) {
        super(clazz);
        this.dimension = dimension;
        this.elementField = field;
        this.one = new OrthogonalMatrix<>(this, one(elementField, dimension));
    }

    static <E extends FieldElement<E>> OrthogonalGroup<E> of(OrthogonalMatrix<E> orthogonalMatrix){
        E[] [] matrix = orthogonalMatrix.matrix;
        final Key key = new Key(matrix.length, matrix[0][0].getStructure());
        return (OrthogonalGroup<E>) INSTANCES.computeIfAbsent(key, k -> new OrthogonalGroup<E>(
            (Class<OrthogonalMatrix<E>>) orthogonalMatrix.getClass(),
            (Field<E>) k.field,
            k.dimension
        ));

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
    public OrthogonalMatrix<E> one() {
        return one;
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
