package org.meeuw.test.math.shapes.dim2;

import lombok.extern.log4j.Log4j2;

import javax.xml.transform.TransformerException;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.shapes.dim2.Circle;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.theories.BasicObjectTheory;

import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

@Log4j2
public class CircleTest implements BasicObjectTheory<Circle<UncertainReal>> {

    public static Circle<UncertainReal> circle = new Circle<>(exactly(1)).times(exactly(2));

    @Test
    public void area() {
        assertThatAlgebraically(circle.area())
            .isEqTo(exactly(4 * Math.PI));
    }

    @Test
    public void perimeter() {
        assertThatAlgebraically(circle.perimeter())
            .isEqTo(exactly(4 * Math.PI));
    }

    @Test
    public void diameter() {
        assertThatAlgebraically(circle.diameter())
            .isEqTo(circle.radius().times(2));
    }

    @Override
    public Arbitrary<@NonNull Circle<UncertainReal>> datapoints() {
        return Arbitraries.doubles().ofScale(3).between(0.001, 1000)
            .map(d -> new Circle<>(element(d)));
    }

    @Test
    public void svg() throws TransformerException {
        //Document svg  = SVG.svg();
        //svg.getDocumentElement().appendChild(SVG.svg(svg, circle.times(20)));
        //log.info("xml:" + SVG.toString(svg));
    }
}
