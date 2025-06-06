package org.meeuw.math.shapes.dim2.svg;

import lombok.extern.java.Log;

import java.io.StringWriter;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.shapes.dim2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Log
public class SVG {


    private static final DocumentBuilder DOCUMENT_BUILDER;

    private static final Transformer TRANSFORMER;
    static {

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder proposal;
        try {
            proposal = documentBuilderFactory.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            log.warning(e.getMessage());
            proposal = null; // will fail later
        }
        DOCUMENT_BUILDER = proposal;
    }
    static {
        final TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer proposal;
        try {
            proposal = transFactory.newTransformer();
            proposal.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            proposal.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (TransformerConfigurationException e) {
            log.warning(e.getMessage());
            proposal = null; // will fail later
        }
        TRANSFORMER = proposal;
    }





    public static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

    private static final Vector2 origin = Vector2.of(100, 100);
    private static final String stroke = "black";


    private SVG() {
        // utility class
    }

    public static  Element svg(Document doc, Circle<?> circle) {
        Element circleElement = doc.createElementNS( SVG_NAMESPACE, "circle");
        circleElement.setAttribute("cx", "" + origin.getX());
        circleElement.setAttribute("cy", "" + origin.getY());
        circleElement.setAttribute("stroke", stroke);
        circleElement.setAttribute("stroke-width", "1");
        circleElement.setAttribute("fill", "none");

        circleElement.setAttribute("r", String.valueOf(circle.radius().intValue()));
        return circleElement;
    }

    public static  Element svg(Document doc, Polygon<?, ?> polygon) {
        Element g = doc.createElementNS( SVG_NAMESPACE, "g");

        Element element = doc.createElementNS(SVG_NAMESPACE, "polygon");
        g.appendChild(element);

        StringBuilder points = new StringBuilder();

        polygon.vertices().forEach(v -> {
            if (points.length() > 0) {
                points.append(" ");
            }
            points.append(origin.getX() + v.getX().doubleValue()).append(",").append(origin.getY() + v.getY().doubleValue());
        });
        element.setAttribute("points", points.toString());
        element.setAttribute("stroke", stroke);
        element.setAttribute("stroke-width", "1");
        element.setAttribute("fill", "none");

        {
            g.appendChild(doc.createComment("Dot showing the first vertex"));

            FieldVector2<?> firstPoint = polygon.vertices().findFirst().get();

            Element dot = doc.createElementNS(SVG_NAMESPACE, "circle");
            dot.setAttribute("cx", String.valueOf(origin.getX() + firstPoint.getX().doubleValue()));
            dot.setAttribute("cy", String.valueOf(origin.getY() + firstPoint.getY().doubleValue()));
            dot.setAttribute("r", "1.1");
            dot.setAttribute("fill", "#ff0000");
            g.appendChild(dot);
        }
        {
            g.appendChild(doc.createComment("Circumscribed circle of " + polygon));
            g.appendChild(circumscribedCircle(doc, polygon));
        }

        return g;
    }
    public static  Element svg(Document doc, RegularPolygon<?> polygon) {
        Element g = svg(doc, (Polygon<?, ?>) polygon);
        {
            g.appendChild(doc.createComment("Inscribed circle of " + polygon));
            g.appendChild(inscribedCircle(doc, polygon));
        }
        return g;
    }


        public static  Element circumscribedCircle(Document doc, Shape<?, ?> shape) {

            LocatedShape<?, ? extends Circle<?>> circleLocatedShape = shape.circumscribedCircle();

            StringBuilder points = new StringBuilder();

            Circle<?> circle = circleLocatedShape.shape();
            FieldVector2<?> offset = circleLocatedShape.location();
            Element circumscribed = doc.createElementNS(SVG_NAMESPACE, "circle");
            circumscribed.setAttribute("cx", String.valueOf(origin.getX() + offset.getX().doubleValue()));
            circumscribed.setAttribute("cy", String.valueOf(origin.getY() + offset.getY().doubleValue()));
            circumscribed.setAttribute("r", "" + circle.radius().doubleValue());
            circumscribed.setAttribute("stroke", stroke);
            circumscribed.setAttribute("stroke-opacity", "0.3");
            circumscribed.setAttribute("stroke-dasharray", "1,1");
            circumscribed.setAttribute("stroke-width", "0.2");
            circumscribed.setAttribute("fill", "none");

        return circumscribed;
    }

    public static  Element inscribedCircle(Document doc, RegularPolygon<?> shape) {

        Circle<?> circle = shape.inscribedCircle();

        StringBuilder points = new StringBuilder();

        Element circumscribed = doc.createElementNS(SVG_NAMESPACE, "circle");
        circumscribed.setAttribute("cx", String.valueOf(origin.getX()));
        circumscribed.setAttribute("cy", String.valueOf(origin.getY()));
        circumscribed.setAttribute("r", "" + circle.radius().doubleValue());
        circumscribed.setAttribute("stroke", stroke);
        circumscribed.setAttribute("stroke-opacity", "0.3");
        circumscribed.setAttribute("stroke-dasharray", "1,1");
        circumscribed.setAttribute("stroke-width", "0.2");
        circumscribed.setAttribute("fill", "none");

        return circumscribed;
    }

    public static Document svg() {
        Document doc = DOCUMENT_BUILDER.newDocument();
        Element rootElement = doc.createElementNS(SVG_NAMESPACE, "svg");
        doc.appendChild(rootElement);
        rootElement.setAttribute("width",  "200");
        rootElement.setAttribute("height",  "200");

        {
            Element g = doc.createElementNS( SVG_NAMESPACE, "g");

            g.appendChild(doc.createComment("Grid"));
            for (int i = 0; i < 200; i += 10) {
                Element line = doc.createElementNS(SVG_NAMESPACE, "line");
                line.setAttribute("x1", String.valueOf(i));
                line.setAttribute("y1", "0");
                line.setAttribute("x2", String.valueOf(i));
                line.setAttribute("y2", "200");
                line.setAttribute("stroke", "#00ff00");
                line.setAttribute("stroke-width", "0.2");
                if (i != 100) {
                    line.setAttribute("stroke-dasharray", "1,1");
                }
                g.appendChild(line);
            }
            for (int i = 0; i < 200; i += 10) {
                Element line = doc.createElementNS(SVG_NAMESPACE, "line");
                line.setAttribute("y1", String.valueOf(i));
                line.setAttribute("x1", "0");
                line.setAttribute("y2", String.valueOf(i));
                line.setAttribute("x2", "200");
                line.setAttribute("stroke", "#00ff00");
                line.setAttribute("stroke-width", "0.2");
                if (i != 100) {
                    line.setAttribute("stroke-dasharray", "1,1");
                }
                g.appendChild(line);
            }
            rootElement.appendChild(g);
        }

        return doc;
    }

    public static void marshal(Document document, StreamResult result) throws TransformerException {
        TRANSFORMER.transform(new DOMSource(document), result);
    }

    public static String toString(Document document) throws TransformerException {
        StringWriter buffer = new StringWriter();
        StreamResult result = new StreamResult(buffer);
        marshal(document, result);
        return buffer.toString();
    }
}
