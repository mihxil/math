package org.meeuw.test.math.shapes.dim2.svg;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;

import org.meeuw.math.shapes.dim2.RegularPolygon;
import org.meeuw.math.shapes.dim2.svg.SVG;
import org.w3c.dom.Document;

import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.element;

@Log4j2
public class SVGTest {


    @Test
    public void regularPolygons() throws Exception {
        File dest = new File(System.getProperty("user.dir"), "../docs/shapes");

        for (int n = 3; n < 20; n++) {
            RegularPolygon<?> nGon = new RegularPolygon<>(n, element(20.0));
            Document svg = SVG.svg();
            svg.getDocumentElement().appendChild(
                SVG.svg(svg, nGon)
            );
            try (FileOutputStream fos = new FileOutputStream(new File(dest,  nGon.n() +"-gon.svg"))) {
                SVG.marshal(svg, new StreamResult(fos));
            }
        }
    }
}
