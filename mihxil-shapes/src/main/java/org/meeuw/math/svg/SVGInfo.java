package org.meeuw.math.svg;

import org.meeuw.math.shapes.dim2.Shape;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public class SVGInfo implements SVGGroup {


    public SVGInfo() {
    }

    @Override
    public Element parent(Element parentG) {
        return parentG.getOwnerDocument().getDocumentElement();
    }

    @Override
    public void fill(SVGDocument svgDocument, Element g) {
        Element info = createElement(g.getOwnerDocument(), "text");
        info.setAttribute("id", "info");
        info.setAttribute("x", String.valueOf(0));
        info.setAttribute("y", String.valueOf(0));
        info.setAttribute("font-size", svgDocument.textSize() +"");
        info.setAttribute("fill", "blue");
        for (Shape<?, ?> shape : svgDocument.shapes()) {
            fill(shape, svgDocument, info);
        }
        g.appendChild(info);

    }

    private void fill(Shape<?, ?> shape, SVGDocument svgDocument, Element info) {
        tspan(svgDocument, info, shape.toString());
        shape.info().forEach(e ->
            tspan(svgDocument, info, e[0] + ": " + e[1])
        );

    }
    protected  void tspan(SVGDocument document, Element info, String text) {
        Element tspan = createElement(info.getOwnerDocument(), "tspan");
        tspan.setAttribute("x", "0");
        tspan.setAttribute("dy", document.textSize()+ "");
        tspan.setTextContent(text);
        info.appendChild(tspan);
    }

}
