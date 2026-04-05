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
package org.meeuw.test.math;

import java.util.*;

import org.junit.jupiter.api.Test;

import org.meeuw.math.ArrayUtils;
import org.meeuw.math.exceptions.InvalidElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.ArrayUtils.minor;
import static org.meeuw.math.ArrayUtils.rotate;

public class ArrayUtilsTest {

    @Test
    public void minorOfSquare() {
        String[][] matrix = new String[][] {
            new String[] {"a", "b", "c"},
            new String[] {"d", "e", "f"},
            new String[] {"g", "h", "i"}
        };
        String[][] minor = minor(String.class, matrix, 1, 1);
        assertThat(minor)
            .hasDimensions(2, 2)
            .isDeepEqualTo(new String[][] {
                new String[] {"a", "c"},
                new String[] {"g", "i"}
            });

        assertThat(minor(String.class, matrix, 0, 0))
            .isDeepEqualTo(new String[][] {
                new String[] {"e", "f"},
                new String[] {"h", "i"}
            });
    }


    @Test
    public void minorOfNonSquare() {
        String[][] matrix = new String[][] {
            new String[] {"a", "b", "c"},
            new String[] {"d", "e", "f"}
        };
        String[][] minor = minor(String.class, matrix, 1, 1);
        assertThat(minor)
            .hasDimensions(1, 2)
            .isDeepEqualTo(new String[][] {
                new String[] {"a", "c"}
            });
    }

    @Test
    public void invalidMinor() {
        String[][] matrix = new String[][] {
            new String[] {"a", "b", "c"},
            new String[] {"d", "e", "f"},
            new String[] {"g", "h", "i"}
        };
        assertThatThrownBy(() -> minor(String.class, matrix, 3, 1)).isInstanceOf(InvalidElementException.class);
        assertThatThrownBy(() -> minor(String.class, matrix, -1, 1)).isInstanceOf(InvalidElementException.class);
        assertThatThrownBy(() -> minor(String.class, matrix, 1, 3)).isInstanceOf(InvalidElementException.class);
        assertThatThrownBy(() -> minor(String.class, matrix, 1, -1)).isInstanceOf(InvalidElementException.class);
    }

    @Test
    public void rotate1() {
        byte[] a = new byte[] {(byte) 1, (byte) 2, 3};
        byte[] b = rotate(a, 1);
        assertThat(b).containsExactly(3, 1, 2);
    }

    @Test
    public void tostring() {
        assertThat(ArrayUtils.toString(new int[] {1, 2, -2})).isEqualTo("(1,2,-2)");
    }

    @Test
    public void byteEquals() {
        assertThat(ArrayUtils.equals(new byte[] {1, 2, -2}, new byte[]{1, 2, -2})).isTrue();
        assertThat(ArrayUtils.equals(new byte[] {1, 2, -2}, new byte[]{1, 2, -3})).isFalse();
        assertThat(ArrayUtils.equals(new byte[] {1, 2, -2}, new byte[]{1, 2, -2, -3})).isFalse();
        byte[] a = new byte[] {1, 2, -2};

        assertThat(ArrayUtils.equals(a, a)).isTrue();
    }

    @Test
    public void removeTrailingZeros() {
        assertThat(ArrayUtils.removeTrailingZeros(new byte[] {1, 2, 0, 0})).containsExactly(1, 2);
        assertThat(ArrayUtils.removeTrailingZeros(new byte[] {1, 2, 3})).containsExactly(1, 2, 3);
        assertThat(ArrayUtils.removeTrailingZeros(new byte[] {0, 0, 0})).isEmpty();
        assertThat(ArrayUtils.removeTrailingZeros(new byte[] {})).isEmpty();
        byte[] noTrail = new byte[] {1, 2, 3};
        assertThat(ArrayUtils.removeTrailingZeros(noTrail)).isSameAs(noTrail);
    }

    @Test
    public void removeTrailingIf() {
        Integer[] arr = {1, 2, 0, 0};
        assertThat(ArrayUtils.removeTrailingIf(i -> i == 0, Integer.class, arr)).containsExactly(1, 2);
        Integer[] noTrail = {1, 2, 3};
        assertThat(ArrayUtils.removeTrailingIf(i -> i == 0, Integer.class, noTrail)).isSameAs(noTrail);
        Integer[] allMatch = {0, 0};
        assertThat(ArrayUtils.removeTrailingIf(i -> i == 0, Integer.class, allMatch)).isEmpty();
    }

    @Test
    public void allEqualTo() {
        assertThat(ArrayUtils.allEqualTo(new byte[] {5, 5, 5}, (byte) 5)).isTrue();
        assertThat(ArrayUtils.allEqualTo(new byte[] {5, 5, 4}, (byte) 5)).isFalse();
        assertThat(ArrayUtils.allEqualTo(new byte[] {}, (byte) 5)).isTrue();
    }

    @Test
    public void determinant2x2() {
        assertThat(ArrayUtils.determinant2x2(1, 2, 3, 4)).isEqualTo(1 * 4 - 2 * 3);
        assertThat(ArrayUtils.determinant2x2(2, 0, 0, 3)).isEqualTo(6.0);
        assertThat(ArrayUtils.determinant2x2(1, 1, 1, 1)).isEqualTo(0.0);
    }

    @Test
    public void adjugate2x2() {
        double[][] m = {{1, 2}, {3, 4}};
        double[][] adj = ArrayUtils.adjugate2x2(m);
        assertThat(adj[0][0]).isEqualTo(4.0);
        assertThat(adj[0][1]).isEqualTo(-2.0);
        assertThat(adj[1][0]).isEqualTo(-3.0);
        assertThat(adj[1][1]).isEqualTo(1.0);
    }

    @Test
    public void cloneMatrix() {
        String[][] original = {{"a", "b"}, {"c", "d"}};
        String[][] clone = ArrayUtils.cloneMatrix(String.class, original);
        assertThat(clone).isNotSameAs(original);
        assertThat(clone).isDeepEqualTo(original);
        clone[0][0] = "x";
        assertThat(original[0][0]).isEqualTo("a");
    }

    @Test
    public void squareMatrix() {
        String[][] m = ArrayUtils.squareMatrix(String.class, "a", "b", "c", "d");
        assertThat(m).hasDimensions(2, 2);
        assertThat(m[0]).containsExactly("a", "b");
        assertThat(m[1]).containsExactly("c", "d");
    }

    @Test
    public void toInverseByteArray() {
        int[] input = {1, 2, 3};
        byte[] result = ArrayUtils.toInverseByteArray(input);
        assertThat(result).containsExactly((byte) 3, (byte) 2, (byte) 1);
    }

    @Test
    public void toArray() {
        List<Integer> list = Arrays.asList(10, 20, 30);
        int[] result = ArrayUtils.toArray(list);
        assertThat(result).containsExactly(10, 20, 30);
    }

    @Test
    public void rotate0() {
        byte[] a = new byte[] {1, 2, 3};
        assertThat(ArrayUtils.rotate(a, 0)).isSameAs(a);
        assertThat(ArrayUtils.rotate(a, 3)).isSameAs(a);
    }

    @Test
    public void shuffleChangesOrder() {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] copy = arr.clone();
        ArrayUtils.shuffle(new Random(42), arr);
        assertThat(arr).containsExactlyInAnyOrder(copy);
    }
}
