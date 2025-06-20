package org.meeuw.math.svg;

import lombok.extern.java.Log;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Log
public class SVG {


    static final DocumentBuilder DOCUMENT_BUILDER;

    static final Transformer TRANSFORMER;

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

    private SVG() {
        // utility class
    }
    static  Element createElement(Document document, String name) {
        return document.createElementNS(SVG_NAMESPACE, name);
    }


    public static void marshal(Document document, StreamResult result) throws TransformerException {
        TRANSFORMER.transform(new DOMSource(document), result);
    }


}
