package org.meeuw.test.math.shapes.d2;

import java.util.List;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.meeuw.math.shapes.d2.NGon;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;


public class NGonTest {
    static NGon<UncertainReal> triangle = new NGon<>(3, exactly(1.0));
    static NGon<UncertainReal> square = new NGon<>(4, exactly(1.0));
    static NGon<UncertainReal> pentagon = new NGon<>(5, exactly(1.0));



    @Test
    public void triangle() {
        assertThat(triangle.n()).isEqualTo(3);
        assertThatAlgebraically(triangle.size()).isEqualTo(exactly(1));

        assertThatAlgebraically(triangle.area()).isEqTo(exactly(Math.sqrt(3)/ 4));
        assertThatAlgebraically(triangle.perimeter()).isEqTo(exactly(3.0));
        assertThatAlgebraically(triangle.interiorAngle()).isEqTo(exactly(Math.PI / 3));
        assertThatAlgebraically(triangle.inscribedRadius()).isEqTo(exactly(Math.sqrt(3) / 6));
        assertThatAlgebraically(triangle.circumscribedRadius()).isEqTo(exactly(Math.sqrt(3) / 3));
        assertThat(triangle.toString()).isEqualTo("{3}, size: 1");
    }

    @Test
    public void square() {
        assertThat(square.n()).isEqualTo(4);
        assertThatAlgebraically(square.area()).isEqTo(exactly(1.0));
        assertThatAlgebraically(square.perimeter()).isEqTo(exactly(4.0));
        assertThatAlgebraically(square.interiorAngle()).isEqTo(exactly(Math.PI / 2));
        assertThatAlgebraically(square.inscribedRadius()).isEqTo(exactly(0.5));
        assertThatAlgebraically(square.circumscribedRadius()).isEqTo(exactly(Math.sqrt(2) / 2));
    }


    public static List<NGon<UncertainReal>> nGons() {
        return List.of(triangle, square, pentagon);
    }

    @ParameterizedTest
    @MethodSource("nGons")
    public void areaIsSizeTimesInscribedRadiusTimesCircumscribedRadius(@ForAll("nGons") NGon<UncertainReal> nGon) {

        UncertainReal ratio = nGon.circumscribedRadius().sqr().minus(nGon.inscribedRadius().sqr());
        assertThatAlgebraically(ratio).isEqTo(exactly(0.25));
    }
}
