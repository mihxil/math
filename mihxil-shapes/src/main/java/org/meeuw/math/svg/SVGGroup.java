package org.meeuw.math.svg;

import java.util.function.BiConsumer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

@FunctionalInterface
public interface SVGGroup extends BiConsumer<SVGDocument, Document> {

    void fill(SVGDocument svgDocument, Element g);


    default Element create(SVGDocument svgDocument, Document document) {
        return createElement(document, "g");
    }

    @Override
    default void accept(SVGDocument svgDocument, Document document) {
        Element g = create(svgDocument, document);
        fill(svgDocument, g);
        document.getDocumentElement().appendChild(g);

    }
}
