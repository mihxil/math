package org.meeuw.math;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.exceptions.InvalidElementException;

/**
 * @author Michiel Meeuwissen
 */
public final class MatrixUtils {

    private  MatrixUtils() {

    }

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

    @SafeVarargs
    public static <E> E[][] squareMatrix(Class<E> element, E... matrix) {
        final int dim = Utils.sqrt(matrix.length);
        final E[][] eMatrix = newMatrix(element, dim, dim);
        for (int i = 0; i < dim; i++) {
            System.arraycopy(matrix, i * dim, eMatrix[i], 0, dim);
        }
        return eMatrix;
    }


    /**
     * Knuth's L algorithm. (taken from https://codereview.stackexchange.com/questions/158798/on-knuths-algorithm-l-to-generate-permutations-in-lexicographic-order).
     *
     * @return Number of swaps. This may be needed if you need to calculate the _sign_ (e.g. Leibniz formula)
     *         0 means no swaps at all. No new permutations can be found.
     */
    public static int permute(int[] values) {
        // Nothing to do for empty or single-element arrays:
        if (values.length <= 1) {
            return 0;
        }

        // L2: Find last j such that self[j] < self[j+1]. Terminate if no such j
        // exists.
        int j = values.length - 2;
        while (j >= 0 && values[j] >= values[j+1]) {
            j -= 1;
        }
        if (j == -1) {
            return 0;
        }

        int swaps = 0;
        // L3: Find last l such that self[j] < self[l], then exchange elements j and l:
        int l = values.length - 1;
        while (values[j] >= values[l]) {
            l -= 1;
        }
        swap(values, j, l);
        swaps++;

        // L4: Reverse elements j+1 ... count-1:
        int lo = j + 1;
        int hi = values.length - 1;
        while (lo < hi) {
            swap(values, lo, hi);
            swaps++;
            lo += 1;
            hi -= 1;
        }
        return swaps;
    }


    private static void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    public static <E> String toString(E[][] matrix) {
          return "(" + Arrays.stream(matrix)
            .map(i ->
                "(" + Arrays.stream(i).map(Object::toString).collect(Collectors.joining(",")) + ")")
            .collect(Collectors.joining(", ")) +
            ")";
    }

}
