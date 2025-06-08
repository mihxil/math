package org.meeuw.math.svg;

import org.meeuw.math.shapes.dim2.Shape;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public class SVGInfo implements SVGGroup {

    private final Shape<?, ?> shape;

    public SVGInfo(Shape<?,?> shape) {
        this.shape = shape;
    }

    @Override
    public void fill(SVGDocument svgDocument, Element g) {
        Element info = createElement(g.getOwnerDocument(), "text");
        info.setAttribute("id", "info");
        info.setAttribute("x", String.valueOf(0));
        info.setAttribute("y", String.valueOf(0));
        info.setAttribute("font-size", svgDocument.textSize() +"");
        info.setAttribute("fill", "blue");
        tspan(svgDocument, info, shape.toString());
        tspan(svgDocument, info, "area: " + shape.area());
        try {
            tspan(svgDocument, info, "perimeter: " + shape.perimeter());
        } catch(Exception e) {
            tspan(svgDocument ,info, "perimeter: " + e.getMessage());
        }
        g.appendChild(info);

    }
    protected  void tspan(SVGDocument document, Element info, String text) {
        Element tspan = createElement(info.getOwnerDocument(), "tspan");
        tspan.setAttribute("x", "0");
        tspan.setAttribute("dy", document.textSize()+ "");
        tspan.setTextContent(text);
        info.appendChild(tspan);
    }

}
