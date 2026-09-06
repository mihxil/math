package org.meeuw.math.svg;

import java.util.function.BiConsumer;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

@FunctionalInterface
public interface SVGGroup<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>
    > extends BiConsumer<SVGDocument<E, C>, Element> {

    void fill(SVGDocument<E, C> svgDocument, Element g);


    default Element create(SVGDocument<E, C> svgDocument, Document document) {
        return createElement(document, "g");
    }

    default void append(Element parentG, Element g) {
        parentG.appendChild(g);
    }

    @Override
    default void accept(SVGDocument<E, C>  svgDocument, Element parentG) {
        Element g = create(svgDocument, parentG.getOwnerDocument());
        fill(svgDocument, g);
        append(parentG, g);
    }
}
