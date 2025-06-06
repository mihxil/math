package org.meeuw.test.math.shapes.dim2.svg;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.math.shapes.dim2.*;
import org.meeuw.math.shapes.dim2.svg.SVG;
import org.w3c.dom.Document;

import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

@Log4j2
public class SVGTest {

    File dest = new File(System.getProperty("user.dir"), "../docs/shapes");

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19})
    public void regularPolygons(int n ) throws Exception {

        RegularPolygon<?> nGon = RegularPolygon.withCircumScribedRadius(n, element(100.0));
        Document svg = SVG.svg();
        svg.getDocumentElement().appendChild(
            SVG.svg(svg, nGon)
        );
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  nGon.n() +"-gon.svg"))) {
            SVG.marshal(svg, new StreamResult(fos));
        }
    }
    @Test
    public void rectangle() throws Exception {

        Rectangle<?> rectangle = new Rectangle<>(element(100.0), element(180.0));
        Document svg = SVG.svg();
        svg.getDocumentElement().appendChild(
            SVG.svg(svg, rectangle)
        );
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "rectangle.svg"))) {
            SVG.marshal(svg, new StreamResult(fos));
        }

    }

    @Test
    public void circle() throws Exception {

        Circle<?> circle = new Circle<>(element(100.0));
        Document svg = SVG.svg();
        svg.getDocumentElement().appendChild(
            SVG.svg(svg, circle)
        );
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "circle.svg"))) {
            SVG.marshal(svg, new StreamResult(fos));
        }

    }
}
