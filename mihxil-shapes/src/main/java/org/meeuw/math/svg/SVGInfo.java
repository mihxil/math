package org.meeuw.math.svg;

import java.util.function.Predicate;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.shapes.Info;
import org.meeuw.math.shapes.Shape;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public class SVGInfo<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>>
    implements SVGGroup<E, C> {

    private final Predicate<Info> filter;

    @lombok.Builder
    private SVGInfo(Predicate<Info> filter) {
        this.filter = filter == null ? i -> true : filter;
    }

    @Override
    public void append(Element parentG, Element g) {
        parentG.getOwnerDocument().getDocumentElement().appendChild(g);
    }

    @Override
    public void fill(SVGDocument<E, C>  svgDocument, Element g) {
        Element info = createElement(g.getOwnerDocument(), "text");
        info.setAttribute("id", "info");
        info.setAttribute("x", String.valueOf(0));
        info.setAttribute("y", String.valueOf(0));
        info.setAttribute("font-size", svgDocument.textSize() +"");
        info.setAttribute("fill", "blue");
        for (Shape<E, C, ?, ?> shape : svgDocument.shapes) {
            fill(shape, svgDocument, info);
        }
        g.appendChild(info);

    }

    @Override
    public void accept(SVGDocument<E, C> svgDocument, Element element) {

    }

    private void fill(Shape<E, C, ?, ?> shape, SVGDocument<E, C> svgDocument, Element info) {
        tspan(svgDocument, info, shape.toString());
        shape.info().filter(filter).forEach(e ->
            tspan(svgDocument, info, e.key() + ": " + e.descriptionString())
        );

    }
    protected  void tspan(SVGDocument<E, C> document, Element info, String text) {
        Element tspan = createElement(info.getOwnerDocument(), "tspan");
        tspan.setAttribute("x", "0");
        tspan.setAttribute("dy", document.textSize()+ "");
        tspan.setTextContent(text);
        info.appendChild(tspan);
    }

}
