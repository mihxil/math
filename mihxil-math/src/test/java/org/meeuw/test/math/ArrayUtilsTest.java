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
}
