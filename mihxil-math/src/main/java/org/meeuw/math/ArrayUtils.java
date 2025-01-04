/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.meeuw.math.exceptions.InvalidElementException;
import org.meeuw.math.validation.Square;

import static java.lang.System.arraycopy;

/**
 * Matrix (2 dimension array) and vector (1 dimension array) utils.
 * @since 0.8
 */
public final class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * For a given matrix, returns a new 'minor' matrix, That is a matrix, where one row, and one column are removed.
     * @param element The type of the elements of the matrix
     * @param matrix The original matrix
     * @param i the row to remove
     * @param j the column to remove
     */
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

    /**
     * For a given vector, returns a new vector with one element removed
     * @param element The type of the elements of the vector
     * @param vector The original vector
     * @param i the element to remove
     */
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

    public static <E> E[][] newSquareMatrix(Class<E> element, int i) {
        return newMatrix(element, i, i);
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
    public static <E> E[][] squareMatrix(Class<E> element, @Square E... matrix) {
        final int dim = IntegerUtils.sqrt(matrix.length);
        final E[][] eMatrix = newMatrix(element, dim, dim);
        for (int i = 0; i < dim; i++) {
            System.arraycopy(matrix, i * dim, eMatrix[i], 0, dim);
        }
        return eMatrix;
    }

    /**
     * <p>
     * Knuth's L algorithm for permutation (taken from <a href="https://codereview.stackexchange.com/questions/158798/on-knuths-algorithm-l-to-generate-permutations-in-lexicographic-order">here</a>).
     * </p>
     * <p>
     * Takes an array of integers. It should contain all the values 0 to length, in any order, and swaps elements to find the 'next' permutation according to Knuth.
     *</p>
     * <p>
     * Calling this function starting with the vector {@code 0...n-1} until it returns {@code 0}, precisely returns all permutations (so this can be done {@code n! - 1} times)
     *</p>
     * @return Number of swaps. This may be needed if you need to calculate the <em>sign</em> (e.g. in Leibniz formula).<br />
     *         0 means no swaps at all. No further permutations can be found, and this terminates the algorithm. In other words,  the input was {@code n-1...0}
     */
    public static int permute(int[] values) {
        // Nothing to do for empty or single-element arrays:
        if (values.length <= 1) {
            return 0;
        }

        // L2: Find last j such that self[j] < self[j+1]. Terminate if no such j
        // exists.
        int j = values.length - 2;
        while (j >= 0 && values[j] >= values[j + 1]) {
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


    public static void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    public static <E> void swap(E[] input, int a, int b) {
        E tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    /**
     * Fisher–Yates shuffle Algorithm
     */
    public static <E> void shuffle(Random random, E[] arr) {
        int n = arr.length;
        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = n - 1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = random.nextInt(i + 1);

            // Swap arr[i] with the element at random index
            swap(arr, i, j);
        }
    }

    public static <E> String toString(E[] array) {
        return toString(array, Object::toString);
    }

    public static  String toString(int[] array) {
        Integer[] a = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            a[i] = Integer.valueOf(array[i]);
        }
        return toString(a, Object::toString);
    }


    /**
     * Takes an array of integers, and converts it to an array of byte's. In inverse order.
     *
     */
    public static byte[] toInverseByteArray(int[] array)  {
        byte[] b = new byte[array.length];
        for (int i = 0 ; i < array.length; i++) {
            b[i] = (byte) array[array.length - 1 - i];
        }
        return b;
    }

    public static <E> String toString(E[] array, Function<E, String> toString) {
        return "(" +
            Arrays.stream(array)
                .map(toString)
                .collect(Collectors.joining(",")) +
            ")";
    }

    public static <E> String toString(E[][] matrix) {
        return toString(matrix, ArrayUtils::toString);
    }

    public static double determinant2x2(
        double a, double b,
        double c, double d) {
        return a * d - b * c;
    }

    public static double[][] adjugate2x2(double[][] values) {
        return  new double[][] {
            new double[] {values[1][1]     , -1 * values[0][1] },
            new double[] {-1 * values[1][0], values[0][0]      }
        };
    }

    public static boolean equals(byte[] a, byte[] b) {
        if (a == b) {
            return true;
        }
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] removeTrailingZeros(byte[] in) {
        int count = 0;
        while(count < in.length && in[in.length - 1 - count] == 0) {
            count++;
        }
        if (count > 0) {
            final byte[] out = new byte[in.length - count];
            final byte[] withoutTrail = new byte[in.length - count];
            arraycopy(in, 0, withoutTrail, 0, in.length - count);
            return withoutTrail;
        } else {
            return in;
        }
    }

   /* public static byte[] removeLeadingZeros(byte[] in) {
        int count = 0;
        while(count < in.length && in[count] == 0) {
            count++;
        }
        if (count > 0) {
            byte[] out = new byte[in.length - count];
            byte[] withoutLead = new byte[in.length - count];
            arraycopy(in, count, withoutLead, 0, in.length - count);
            return withoutLead;
        } else {
            return in;
        }
    }*/

    public static byte[] rotate(byte[] in, int amount) {
        amount %= in.length;
        if (amount == 0) {
            return in;
        }
        byte[] out = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            out[(i + amount) % out.length ] = in[i];
        }
        return out;
    }

    public static boolean allEqualTo(byte[] in, byte value) {
        for (byte b: in ) {
            if (b != value) {
                return false;
            }
        }
        return true;
    }


}
