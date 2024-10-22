package org.meeuw.test.math.abstractalgebra.dihedral;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.dihedral.DihedralGroup;
import org.meeuw.math.abstractalgebra.dihedral.DihedralSymmetry;
import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.theories.abstractalgebra.GroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Log4j2
public class DihedralGroupTest {

    @Test
    public void apply() {
        DihedralSymmetry r1 = DihedralSymmetry.r(1, 4);
        Vector2 v2 = r1.apply(Vector2.of(0.1, 1));
        assertThat(v2.toString()).isEqualTo("(-1, 0.1)");
    }

    @Test
    public void s() {
        DihedralSymmetry s1 = DihedralSymmetry.s(1, 4);
        Vector2 v2 = s1.apply(Vector2.of(0.1, 1));
        log.info("{}", v2);
        assertThat(v2.toString()).isEqualTo("(1, 0.1)");

    }
    @Test
    public void illegal() {
        DihedralGroup group = DihedralGroup.of(4);
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


    public static class D1Test implements GroupTheory<DihedralSymmetry> {
        @Override
        public Arbitrary<? extends DihedralSymmetry> elements() {
            return Arbitraries.of(DihedralGroup.of(1).stream().toList());
        }
    }

    public static class D2Test implements GroupTheory<DihedralSymmetry> {
        @Override
        public Arbitrary<? extends DihedralSymmetry> elements() {
            return Arbitraries.of(DihedralGroup.of(2).stream().toList());
        }
    }


    public static class TriangleSymmetryTest implements GroupTheory<DihedralSymmetry> {
        @Override
        public Arbitrary<? extends DihedralSymmetry> elements() {
            return Arbitraries.of(DihedralGroup.of(3).stream().toList());
        }
    }

    public static class SquareSymmetryTest implements GroupTheory<DihedralSymmetry> {
        @Override
        public Arbitrary<? extends DihedralSymmetry> elements() {
            return Arbitraries.of(DihedralGroup.of(4).stream().toList());
        }
    }


    public static class HeptagonSymmetryTests implements GroupTheory<DihedralSymmetry> {
        @Override
        public Arbitrary<? extends DihedralSymmetry> elements() {
            return Arbitraries.of(DihedralGroup.of(5).stream().toList());
        }
    }

    public static class HexagonSymmetryTests implements GroupTheory<DihedralSymmetry> {
        @Override
        public Arbitrary<? extends DihedralSymmetry> elements() {
            return Arbitraries.of(DihedralGroup.of(6).stream().toList());
        }
    }
}
