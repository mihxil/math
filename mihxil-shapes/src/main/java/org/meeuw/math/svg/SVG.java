package org.meeuw.math.svg;

import lombok.extern.java.Log;

import java.io.StringWriter;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
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

    private static final Rectangle<ModuloFieldElement> gridSize = Rectangle.of(200, 200);
    private static final Rectangle<ModuloFieldElement> spacing = Rectangle.of(10, 10);

    private static final Vector2 origin = new Vector2(gridSize.width().doubleValue()/ 2, gridSize.height().doubleValue() / 2); // the center of the grid
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

        circleElement.setAttribute("r", String.valueOf(circle.radius().doubleValue()));
        doc.getDocumentElement().appendChild(circleElement);
        return circleElement;
    }

    public static  Element svg(Document doc, Ellipse<?> ellipse) {
        Element g = doc.createElementNS( SVG_NAMESPACE, "g");
        doc.getDocumentElement().appendChild(g);

        Element element = doc.createElementNS( SVG_NAMESPACE, "ellipse");
        element.setAttribute("cx", "" + origin.getX());
        element.setAttribute("cy", "" + origin.getY());
        element.setAttribute("stroke", stroke);
        element.setAttribute("stroke-width", "1");
        element.setAttribute("fill", "none");

        element.setAttribute("rx", String.valueOf(ellipse.radiusx().doubleValue()));
        element.setAttribute("ry", String.valueOf(ellipse.radiusy().doubleValue()));
        g.appendChild(element);

        {
            g.appendChild(doc.createComment("Circumscribed circle of " + ellipse));
            g.appendChild(circumscribedCircle(doc, ellipse));
        }


        return g;
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
        doc.getDocumentElement().appendChild(g);

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

        doc.getDocumentElement().appendChild(circumscribed);
        return circumscribed;
    }

    public static  Element inscribedCircle(Document doc, RegularPolygon<?> shape) {
        Circle<?> circle = shape.inscribedCircle();
        StringBuilder points = new StringBuilder();
        Element inscribed = doc.createElementNS(SVG_NAMESPACE, "circle");
        inscribed.setAttribute("cx", String.valueOf(origin.getX()));
        inscribed.setAttribute("cy", String.valueOf(origin.getY()));
        inscribed.setAttribute("r", "" + circle.radius().doubleValue());
        inscribed.setAttribute("stroke", stroke);
        inscribed.setAttribute("stroke-opacity", "0.3");
        inscribed.setAttribute("stroke-dasharray", "1,1");
        inscribed.setAttribute("stroke-width", "0.2");
        inscribed.setAttribute("fill", "none");

        doc.getDocumentElement().appendChild(inscribed);

        return inscribed;
    }

    public static Document svg() {
        Document doc = DOCUMENT_BUILDER.newDocument();
        Element rootElement = doc.createElementNS(SVG_NAMESPACE, "svg");
        doc.appendChild(rootElement);
        rootElement.setAttribute("width",  String.valueOf(gridSize.width().intValue()));
        rootElement.setAttribute("height", String.valueOf(gridSize.height().intValue()));

        {
            Element g = doc.createElementNS( SVG_NAMESPACE, "g");
            g.setAttribute("id", "grid");
            g.appendChild(doc.createComment("Grid"));
            for (int i = 0; i < gridSize.width().intValue(); i += spacing.width().intValue()) {
                Element line = doc.createElementNS(SVG_NAMESPACE, "line");
                line.setAttribute("x1", String.valueOf(i));
                line.setAttribute("y1", "0");
                line.setAttribute("x2", String.valueOf(i));
                line.setAttribute("y2", gridSize.height().intValue() + "");
                line.setAttribute("stroke", "#00ff00");
                line.setAttribute("stroke-width", "0.2");
                if (i != 100) {
                    line.setAttribute("stroke-dasharray", "1,1");
                }
                g.appendChild(line);
            }
            for (int i = 0; i < gridSize.height().intValue(); i += spacing.height().intValue()) {
                Element line = doc.createElementNS(SVG_NAMESPACE, "line");
                line.setAttribute("y1", String.valueOf(i));
                line.setAttribute("x1", "0");
                line.setAttribute("y2", String.valueOf(i));
                line.setAttribute("x2", gridSize.width().intValue() + "");
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
