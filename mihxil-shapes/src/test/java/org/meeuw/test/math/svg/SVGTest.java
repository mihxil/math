package org.meeuw.test.math.svg;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

// tag::imports[]

import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.shapes.dim2.*;
import org.meeuw.math.svg.SVG;
import org.meeuw.math.svg.SVGDocument;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.meeuw.math.svg.SVGDocument.defaultSVG;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

// end::imports[]

@Log4j2
public class SVGTest {

    Rectangle<ModuloFieldElement> size = Rectangle.of(205, 205);
    Rectangle<ModuloFieldElement> spacing = Rectangle.of(10, 10);
    File dest = new File(System.getProperty("user.dir"), "../docs/shapes");

    // tag::regularPolygons[]
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    public void regularPolygons(int n ) throws Exception {

        RegularPolygon<UncertainReal> polygon = RegularPolygon.withCircumScribedRadius(n, element(100.0));

        SVGDocument document = defaultSVG()
            .withSize(size)
            .addGrid(b -> b.spacing(spacing))
            .addInfo()
            .addRegularPolygon(polygon, s -> s
                .circumscribedCircle(true)
                .circumscribedRectangle(true)
                .inscribedCircle(true)
            )
            ;

        try (FileOutputStream fos = new FileOutputStream(new File(dest,  n +"-gon.svg"))) {
            SVG.marshal(document.buildDocument(), new StreamResult(fos));
        }
    }
    // end::regularPolygons[]

    @Test
    public void rotatedPolygon() throws Exception {

        RegularPolygon<UncertainReal> polygon = RegularPolygon.withCircumScribedRadius(3, element(100.0)).rotate(element(Math.toRadians(10.0)));

        SVGDocument document = defaultSVG()
            .withSize(size)
            .addGrid(b -> b.spacing(spacing))
            .addInfo()
            .addRegularPolygon(polygon, s -> s
                .circumscribedCircle(true)
                .circumscribedRectangle(true)
                .inscribedCircle(true)
            )
            ;

        try (FileOutputStream fos = new FileOutputStream(new File(dest,   "rotated-3-gon.svg"))) {
            SVG.marshal(document.buildDocument(), new StreamResult(fos));
        }
    }


    // tag::otherShapes[]
    @Test
    public void rectangle() throws Exception {
        Rectangle<UncertainReal> rectangle = new Rectangle<>(element(100.0), element(170.0)).rotate(element(Math.toRadians(10.0)));

        SVGDocument svg = defaultSVG()
            .withSize(size)
            .addGrid(b -> b.spacing( spacing))
            .addInfo()
            .addPolygon(rectangle, s -> {
                s.circumscribedCircle(true);
                s.circumscribedRectangle(true);

            });
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "rectangle.svg"))) {
            SVG.marshal(svg.buildDocument(), new StreamResult(fos));
        }
    }

    @Test
    public void circle() throws Exception {
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "circle.svg"))) {
            SVG.marshal(defaultSVG()
                .withSize(size)
                .addGrid(b -> b.spacing(spacing))
                .addInfo()
                .addCircle(new Circle<>(element(100.0)), s -> s
                    .circumscribedRectangle(true)
                    .circumscribedRectangleAttributes(e -> {
                        e.setAttribute("stroke", "red");
                        }
                    )
                )
                .buildDocument(),
                new StreamResult(fos)
            );
        }
    }

    @Test
    public void ellipse() throws Exception {
        Ellipse<UncertainReal> ellipse = new Ellipse<>(element(100.0), element(80.0), element(Math.toRadians(45.0)));

        SVGDocument document = defaultSVG()
            .withSize(size)
            .addGrid(b -> b.spacing(spacing))
            .addInfo()
            .addEllipse(ellipse, s -> s
                .circumscribedCircle(true)
                .circumscribedRectangle(true)
            );
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "ellipse.svg"))) {
          SVG.marshal(document.buildDocument(), new StreamResult(fos));
        }
    }
    // end::otherShapes[]

}
