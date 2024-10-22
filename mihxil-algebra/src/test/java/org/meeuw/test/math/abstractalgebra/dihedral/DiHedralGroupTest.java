package org.meeuw.test.math.abstractalgebra.dihedral;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.dihedral.DiHedralGroup;
import org.meeuw.math.abstractalgebra.dihedral.DiHedralSymmetry;
import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.theories.abstractalgebra.GroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Log4j2
public class DiHedralGroupTest {

    @Test
    public void apply() {
        DiHedralSymmetry r1 = DiHedralSymmetry.r(1, 4);
        Vector2 v2 = r1.apply(Vector2.of(0.1, 1));
        assertThat(v2.toString()).isEqualTo("(-1, 0.1)");
    }

    @Test
    public void s() {
        DiHedralSymmetry s1 = DiHedralSymmetry.s(1, 4);
        Vector2 v2 = s1.apply(Vector2.of(0.1, 1));
        log.info("{}", v2);
        assertThat(v2.toString()).isEqualTo("(1, 0.1)");

    }
    @Test
    public void illegal() {
        DiHedralGroup group = DiHedralGroup.of(4);
        assertThatThrownBy(() -> {
            group.s(4);
        }).isInstanceOf(InvalidElementCreationException.class);
        assertThatThrownBy(() -> {
            group.r(4);
        }).isInstanceOf(InvalidElementCreationException.class);
        assertThatThrownBy(() -> {
            group.r(-1);
        }).isInstanceOf(InvalidElementCreationException.class);

    }


    public static class D1Test implements GroupTheory<DiHedralSymmetry> {
        @Override
        public Arbitrary<? extends DiHedralSymmetry> elements() {
            return Arbitraries.of(DiHedralGroup.of(1).stream().toList());
        }
    }

    public static class D2Test implements GroupTheory<DiHedralSymmetry> {
        @Override
        public Arbitrary<? extends DiHedralSymmetry> elements() {
            return Arbitraries.of(DiHedralGroup.of(2).stream().toList());
        }
    }


    public static class TriangleSymmetryTest implements GroupTheory<DiHedralSymmetry> {
        @Override
        public Arbitrary<? extends DiHedralSymmetry> elements() {
            return Arbitraries.of(DiHedralGroup.of(3).stream().toList());
        }
    }

    public static class SquareSymmetryTest implements GroupTheory<DiHedralSymmetry> {
        @Override
        public Arbitrary<? extends DiHedralSymmetry> elements() {
            return Arbitraries.of(DiHedralGroup.of(4).stream().toList());
        }
    }


    public static class HeptagonSymmetryTests implements GroupTheory<DiHedralSymmetry> {
        @Override
        public Arbitrary<? extends DiHedralSymmetry> elements() {
            return Arbitraries.of(DiHedralGroup.of(5).stream().toList());
        }
    }

    public static class HexagonSymmetryTests implements GroupTheory<DiHedralSymmetry> {
        @Override
        public Arbitrary<? extends DiHedralSymmetry> elements() {
            return Arbitraries.of(DiHedralGroup.of(6).stream().toList());
        }
    }
}
