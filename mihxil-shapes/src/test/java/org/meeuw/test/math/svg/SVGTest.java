package org.meeuw.test.math.svg;

import lombok.extern.java.Log;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

// tag::imports[]

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.shapes.dim2.*;
import org.meeuw.math.svg.SVG;

import static org.meeuw.math.svg.SVGDocument.default2DDocument;

import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;


// end::imports[]

@Log
public class SVGTest {

    Rectangle<RationalNumber, BigDecimalElement> size = Rectangle.of(205, 205);
    Rectangle<RationalNumber, BigDecimalElement> spacing = Rectangle.of(10, 0);
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

        RegularPolygon<BigDecimalElement, BigDecimalElement> polygon = RegularPolygon.<RationalNumber, BigDecimalElement>
            withCircumScribedRadius(n,
            RationalNumber.of(100));

        var document = default2DDocument()
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
            SVG.marshal(default2DDocument()
                .withSize(size)
                .addGrid(b -> b.spacing(spacing))
                .addInfo()
                .addRegularPolygon(
                    RegularPolygon
                        .withCircumScribedRadius(3, element(size.width().doubleValue() / 2))
                        .rotate(element(Math.toRadians(10.0))),
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
        Rectangle<RationalNumber, BigDecimalElement> rectangle = new Rectangle<>(
            RationalNumber.of(100),
            RationalNumber.of(170))
            .rotate(element(Math.toRadians(10.0)));

        var svg = default2DDocument()
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
            SVG.marshal(default2DDocument()
                .withSize(size)
                .addGrid(b -> b.spacing(spacing))
                .addInfo()
                .addCircle(new Circle<>(RationalNumber.of(100)), s -> s
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
        Ellipse<RationalNumber, BigDecimalElement> ellipse = new Ellipse<>(
            RationalNumber.of(100),RationalNumber.of (80),
            RationalNumber.of(Math.toRadians(45.0)));

        var document = default2DDocument()
            .withSize(size)
            .addGrid(b -> b.spacing(spacing))
            //.addInfo()
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
