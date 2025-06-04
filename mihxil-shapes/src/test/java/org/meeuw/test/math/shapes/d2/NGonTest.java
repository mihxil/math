package org.meeuw.test.math.shapes.d2;

import java.util.List;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.shapes.d2.Circle;
import org.meeuw.math.shapes.d2.NGon;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.theories.BasicObjectTheory;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;


public class NGonTest implements BasicObjectTheory<NGon<UncertainReal>> {
    static NGon<UncertainReal> triangle = new NGon<>(3, element(1.0));
    static NGon<UncertainReal> square = new NGon<>(4, element(1.0));
    static NGon<UncertainReal> pentagon = new NGon<>(5, element(1.0));



    @Test
    public void triangle() {
        assertThat(triangle.n()).isEqualTo(3);
        assertThatAlgebraically(triangle.size()).isEqualTo(element(1));

        assertThatAlgebraically(triangle.area()).isEqTo(element(Math.sqrt(3)/ 4));
        assertThatAlgebraically(triangle.perimeter()).isEqTo(element(3.0));
        assertThatAlgebraically(triangle.interiorAngle()).isEqTo(element(Math.PI / 3));
        assertThatAlgebraically(triangle.inscribedRadius()).isEqTo(element(Math.sqrt(3) / 6));
        assertThatAlgebraically(triangle.circumscribedRadius()).isEqTo(element(Math.sqrt(3) / 3));
        assertThat(triangle.toString()).isEqualTo("{3}, size: 1");
    }

    @Test
    public void square() {
        assertThat(square.n()).isEqualTo(4);
        assertThatAlgebraically(square.area()).isEqTo(element(1.0));
        assertThatAlgebraically(square.perimeter()).isEqTo(element(4.0));
        assertThatAlgebraically(square.interiorAngle()).isEqTo(element(Math.PI / 2));
        assertThatAlgebraically(square.inscribedRadius()).isEqTo(element(0.5));
        assertThat(square.circumscribedCircle()).isEqualTo(new Circle<>(element(Math.sqrt(2) / 2)));
    }


    public static List<NGon<UncertainReal>> nGons() {
        return List.of(triangle, square, pentagon);
    }

    @ParameterizedTest
    @MethodSource("nGons")
    public void areaIsSizeTimesInscribedRadiusTimesCircumscribedRadius(@ForAll("nGons") NGon<UncertainReal> nGon) {

        UncertainReal ratio = nGon.circumscribedRadius().sqr().minus(nGon.inscribedRadius().sqr());
        assertThatAlgebraically(ratio).isEqTo(element(0.25));
    }

    @Override
    public Arbitrary<@NonNull NGon<UncertainReal>> datapoints() {
        return Arbitraries.integers().between(3, 20)
            .flatMap(n -> Arbitraries.doubles().ofScale(3).between(0.001, 1000)
                .map(size -> new NGon<>(n, element(size))));
    }
}
