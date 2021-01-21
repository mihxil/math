package org.meeuw.math.text;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
class FixedSizeMapTest {

    @Test
    public void test() {
        Map<String, Integer> test = new LinkedHashMap<>();
        test.put("a", 1);
        test.put("b", 1);
        FixedSizeMap<String, Integer> fixed = new FixedSizeMap<>(test);

        fixed.put("a", 2);
        assertThatThrownBy(() -> fixed.put("c", 3)).isInstanceOf(UnsupportedOperationException.class);

        assertThat(fixed.size()).isEqualTo(2);
        assertThat(fixed.get("a")).isEqualTo(2);

        assertThat(fixed.toString()).isEqualTo("{a=2, b=1}");

    }

}
