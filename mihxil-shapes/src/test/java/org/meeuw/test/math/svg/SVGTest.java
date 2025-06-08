package org.meeuw.test.math.svg;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.math.shapes.dim2.*;
import org.meeuw.math.svg.*;

import org.meeuw.math.uncertainnumbers.field.*;

import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

// end::imports[]

@Log4j2
public class SVGTest {

    File dest = new File(System.getProperty("user.dir"), "../docs/shapes");

    // tag::regularPolygons[]
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    public void regularPolygons(int n ) throws Exception {

        RegularPolygon<UncertainReal> polygon = RegularPolygon.withCircumScribedRadius(n, element(100.0));

        SVGDocument document = SVGDocument.builder()
            .size(Rectangle.of(200, 200))
            .build()
            .grid(b -> b.spacing(Rectangle.of(10, 10)))
            .info(polygon)
            .regularPolygon(polygon, s ->
                s.circumscribedCircle(true)
                    .inscribedCircle(true)
            )
            ;

        try (FileOutputStream fos = new FileOutputStream(new File(dest,  n +"-gon.svg"))) {
            SVG.marshal(document.document(), new StreamResult(fos));
        }
    }
    // end::regularPolygons[]

    // tag::otherShapes[]

    @Test
    public void rectangle() throws Exception {
        Rectangle<UncertainReal> rectangle = new Rectangle<>(element(100.0), element(180.0));

        SVGDocument document = SVGDocument.builder()
            .size(Rectangle.of(200, 200))
            .build()
            .grid(b -> b.spacing(Rectangle.of(10, 10)))
            .info(rectangle)
            .polygon(rectangle, s -> {
                s.circumscribedCircle(true);

            });
        //Document svg = SVG.svg();
        //SVG.svg(svg, rectangle);
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "rectangle.svg"))) {
            SVG.marshal(document.document(), new StreamResult(fos));
        }
    }

    @Test
    public void circle() throws Exception {
        Circle<UncertainReal> circle = new Circle<>(element(100.0));

          SVGDocument document = SVGDocument.builder()
            .size(Rectangle.of(200, 200))
            .build()
            .grid(b -> b.spacing(Rectangle.of(10, 10)))
            .info(circle)
            .circle(circle, s -> {})
            ;


        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "circle.svg"))) {
            SVG.marshal(document.document(), new StreamResult(fos));
        }
    }

    @Test
    public void ellipse() throws Exception {
        Ellipse<UncertainReal> ellipse = new Ellipse<>(element(100.0), element(80.0));


        SVGDocument document = SVGDocument.builder()
            .size(Rectangle.of(200, 200))
            .build()
            .grid(b -> b.spacing(Rectangle.of(10, 10)))
            .info(ellipse)
            .ellipse(ellipse, s -> {s.circumscribedCircle(true);});
        //Document svg = SVG.svg();
        //SVG.svg(svg, ellipse);
        try (FileOutputStream fos = new FileOutputStream(new File(dest,  "ellipse.svg"))) {
          SVG.marshal(document.document(), new StreamResult(fos));
        }
    }
    // end::otherShapes[]

}
