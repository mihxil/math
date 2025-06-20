package org.meeuw.math.svg;

import java.util.function.BiConsumer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

@FunctionalInterface
public interface SVGGroup extends BiConsumer<SVGDocument, Element> {

    void fill(SVGDocument svgDocument, Element g);


    default Element create(SVGDocument svgDocument, Document document) {
        return createElement(document, "g");
    }

    default void append(Element parentG, Element g) {
        parentG.appendChild(g);
    }

    @Override
    default void accept(SVGDocument svgDocument, Element parentG) {
        Element g = create(svgDocument, parentG.getOwnerDocument());
        fill(svgDocument, g);
        append(parentG, g);
    }
}
