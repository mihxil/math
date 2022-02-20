package org.meeuw.math;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.exceptions.InvalidElementException;

/**
 * @author Michiel Meeuwissen
 */
public final class MatrixUtils {


    public static <E> E[][] minor(Class<E> element, E[][] matrix, int i, int j) {
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[i].length) {
            throw new InvalidElementException(i + "," + j + " not an index of " + matrix.length + "x" + matrix[0].length + " matrix");
        }
        E[][] minor = newMatrix(element, matrix.length - 1, matrix[0].length - 1);
        for (int k = 0; k < minor.length; k++) {
            minor[k] = delete(element, matrix[k < i ? k : k + 1], j);
        }
        return minor;

    }
    @SuppressWarnings("unchecked")
    public static <E> E[] delete(Class<E> element, E[] vector, int i) {
        E[] shorter = (E[]) Array.newInstance(element, vector.length - 1);
        System.arraycopy(vector, 0, shorter, 0, i);

        // copy elements from original array from index+1 till end into copyArray
        System.arraycopy(vector, i + 1, shorter, i, vector.length - i - 1);

        return shorter;
    }

    @SuppressWarnings("unchecked")
    public static <E> E[][] newMatrix(Class<E> element, int i, int j) {
        return (E[][]) Array.newInstance(element, i, j);
    }

    public static <E> E[][] cloneMatrix(Class<E> element, E[][] matrix) {
        E[][] result = newMatrix(element, matrix.length, matrix[0].length);
        for(int i = 0; i < matrix.length; i++) {
            E[] aMatrix = matrix[i];
            int   aLength = aMatrix.length;
            System.arraycopy(aMatrix, 0, result[i], 0, aLength);
        }
        return result;
    }

    public static <E> String toString(E[][] matrix) {
          return "(" + Arrays.stream(matrix)
            .map(i ->
                "(" + Arrays.stream(i).map(Object::toString).collect(Collectors.joining(",")) + ")")
            .collect(Collectors.joining(", ")) +
            ")";
    }

}
