package org.meeuw.test.math.svg;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim2.*;
import org.meeuw.math.svg.SVG;
import org.meeuw.math.svg.SVGDocument;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.meeuw.math.abstractalgebra.reals.RealField.element;
import static org.meeuw.math.svg.SVGDocument.defaultSVG;

// end::imports[]

@Log4j2
public class SVGTest {

    Rectangle<ModuloFieldElement> size = Rectangle.of(205, 205);
    Rectangle<ModuloFieldElement> spacing = Rectangle.of(10, 10);
    File dest = new File(System.getProperty("user.dir"), "../docs/shapes");

    @BeforeEach
    public void setUp() {
        ConfigurationService.defaultConfiguration(c -> {
            c.configure(UncertaintyConfiguration.class, ac -> {
                return ac.withNotation(UncertaintyConfiguration.Notation.ROUND_VALUE);
            }).configure(NumberConfiguration.class, nc -> {
                return nc.withMaximalPrecision(2);
            });
        });
    }

    // tag::regularPolygons[]
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    public void regularPolygons(int n ) throws Exception {

        RegularPolygon<RealNumber> polygon = RegularPolygon.withCircumScribedRadius(n, element(100.0));

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
            SVG.marshal(document, new StreamResult(fos));
        }
    }

    @Test
    public void rotatedPolygon() throws Exception {
        try (FileOutputStream fos = new FileOutputStream(new File(dest,   "rotated-3-gon.svg"))) {
            SVG.marshal(defaultSVG()
                .withSize(size)
                .addGrid(b -> b.spacing(spacing))
                .addInfo()
                .addRegularPolygon(
                    RegularPolygon.withCircumScribedRadius(3, element(size.width().doubleValue() / 2)).rotate(element(Math.toRadians(10.0))),
                    s -> s
                        .circumscribedCircle(true)
                        .circumscribedRectangle(true)
                        .inscribedCircle(true)
                ), new StreamResult(fos));
        }
    }

    // end::regularPolygons[]


    // tag::otherShapes[]
    @Test
    public void rectangle() throws Exception {
        Rectangle<RealNumber> rectangle = new Rectangle<>(element(100.0), element(170.0)).rotate(element(Math.toRadians(10.0)));

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
        Ellipse<RealNumber> ellipse = new Ellipse<>(element(100.0), element(80.0), element(Math.toRadians(45.0)));

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
