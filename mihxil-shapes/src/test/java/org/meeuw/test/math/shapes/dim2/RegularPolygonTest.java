package org.meeuw.test.math.shapes.dim2;

import lombok.extern.log4j.Log4j2;

import java.util.List;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.dihedral.DihedralGroup;
import org.meeuw.math.shapes.dim2.Circle;
import org.meeuw.math.shapes.dim2.RegularPolygon;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;


@Log4j2
public class RegularPolygonTest implements ShapeTheory<RegularPolygon<UncertainReal>> {
    static RegularPolygon<UncertainReal> triangle = RegularPolygon.of(DihedralGroup.of(3), element(1.0));
    static RegularPolygon<UncertainReal> square = new RegularPolygon<>(4, element(1.0));
    static RegularPolygon<UncertainReal> pentagon = new RegularPolygon<>(5, element(1.0));

    static RegularPolygon<UncertainReal> hexagon = new RegularPolygon<>(6, element(1.0));
    static RegularPolygon<UncertainReal> heptagon = new RegularPolygon<>(7, element(1.0));




    @Test
    public void triangle() {
        assertThat(triangle.n()).isEqualTo(3);
        assertThat(triangle.numberOfEdges()).isEqualTo(3);
        assertThatAlgebraically(triangle.size()).isEqualTo(element(1));

        assertThatAlgebraically(triangle.area()).isEqTo(element(Math.sqrt(3)/ 4));
        assertThatAlgebraically(triangle.perimeter()).isEqTo(element(3.0));
        assertThatAlgebraically(triangle.interiorAngle()).isEqTo(element(Math.PI / 3));
        assertThatAlgebraically(triangle.inscribedRadius()).isEqTo(element(Math.sqrt(3) / 6));
        assertThatAlgebraically(triangle.circumscribedRadius()).isEqTo(element(Math.sqrt(3) / 3));
        assertThat(triangle.toString()).isEqualTo("{3}");
    }

    @Test
    public void square() {
        assertThat(square.n()).isEqualTo(4);
        assertThatAlgebraically(square.area()).isEqTo(element(1.0));
        assertThatAlgebraically(square.perimeter()).isEqTo(element(4.0));
        assertThatAlgebraically(square.interiorAngle()).isEqTo(element(Math.PI / 2));
        assertThatAlgebraically(square.inscribedRadius()).isEqTo(element(0.5));
        assertThat(square.circumscribedCircle().shape()).isEqualTo(new Circle<>(element(Math.sqrt(2) / 2)));
    }


    public static List<RegularPolygon<UncertainReal>> nGons() {
        return List.of(triangle, square, pentagon, hexagon,
            heptagon,
            new RegularPolygon<>(8, element(1.0)),
            new RegularPolygon<>(9, element(1.0)),
            new RegularPolygon<>(10, element(1.0)),
            new RegularPolygon<>(11, element(1.0)),
            new RegularPolygon<>(12, element(1.0))
        );
    }

    @ParameterizedTest
    @MethodSource("nGons")
    public void areaIsSizeTimesInscribedRadiusTimesCircumscribedRadius(@ForAll("nGons") RegularPolygon<UncertainReal> nGon) {

        UncertainReal ratio = nGon.circumscribedRadius().sqr().minus(nGon.inscribedRadius().sqr());
        assertThatAlgebraically(ratio).isEqTo(element(0.25));
    }

    @ParameterizedTest
    @MethodSource("nGons")
    public void vertices(@ForAll("nGons") RegularPolygon<UncertainReal> nGon) {
        nGon.vertices().forEach(fv -> {
            log.info("{}", fv);
        });
    }

    @Override
    public Arbitrary<@NonNull RegularPolygon<UncertainReal>> datapoints() {
        return Arbitraries.integers().between(3, 20)
            .flatMap(n -> Arbitraries.doubles().ofScale(3).between(0.001, 1000)
                .map(size -> new RegularPolygon<>(n, element(size))));
    }
}
