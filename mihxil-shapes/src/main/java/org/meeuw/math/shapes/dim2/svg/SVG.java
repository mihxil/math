package org.meeuw.math.shapes.dim2.svg;

import lombok.extern.java.Log;

import java.io.StringWriter;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.shapes.dim2.Circle;
import org.meeuw.math.shapes.dim2.Polygon;
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
        } catch (TransformerConfigurationException e) {
            log.warning(e.getMessage());
            proposal = null; // will fail later
        }
        TRANSFORMER = proposal;
    }





    public static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";


    private SVG() {
        // utility class
    }

    public static  Element svg(Document doc, Circle<?> circle) throws ParserConfigurationException {
        Element circleElement = doc.createElementNS( SVG_NAMESPACE, "circle");
        circleElement.setAttribute("cx", "100");
        circleElement.setAttribute("cy", "100");
        circleElement.setAttribute("stroke", "black");
        circleElement.setAttribute("stroke-width", "2");
        circleElement.setAttribute("fill", "none");

        circleElement.setAttribute("r", String.valueOf(circle.radius().intValue()));
        return circleElement;
    }

    public static  Element svg(Document doc, Polygon<?, ?> polygon) throws ParserConfigurationException {
        Element g = doc.createElementNS( SVG_NAMESPACE, "g");

        Element element = doc.createElementNS(SVG_NAMESPACE, "polygon");
        g.appendChild(element);

        StringBuilder points = new StringBuilder();

        polygon.vertices().forEach(v -> {
            if (points.length() > 0) {
                points.append(" ");
            }
            points.append(100 + v.getX().intValue()).append(",").append(100 + v.getY().intValue());
        });
        element.setAttribute("points", points.toString());
        element.setAttribute("stroke", "black");
        element.setAttribute("stroke-width", "2");
        element.setAttribute("fill", "none");

        FieldVector2<?> firstPoint = polygon.vertices().findFirst().get();

        Element dot = doc.createElementNS(SVG_NAMESPACE, "circle");
        dot.setAttribute("cx", String.valueOf(100 + firstPoint.getX().intValue()));
        dot.setAttribute("cy", String.valueOf(100 + firstPoint.getY().intValue()));
        dot.setAttribute("r", "2");
        dot.setAttribute("color", "red");
        g.appendChild(dot);


        return g;
    }

    public static Document svg() {
        Document doc = DOCUMENT_BUILDER.newDocument();
        Element rootElement = doc.createElementNS(SVG_NAMESPACE, "svg");
        doc.appendChild(rootElement);
        rootElement.setAttribute("width",  "200");
        rootElement.setAttribute("height",  "200");
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
