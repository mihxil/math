package org.meeuw.math.svg;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.shapes.dim2.Ellipse;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public class SVGEllipse<F extends CompleteScalarFieldElement<F>> extends SVGShape<Ellipse<F>> {


    private final boolean subscribedCircle;

    @lombok.Builder
    public SVGEllipse(Ellipse<F> ellipse, boolean circumscribedCircle) {
        super(ellipse, circumscribedCircle);
        this.subscribedCircle = circumscribedCircle;
    }

    @Override
    public void fillShape(SVGDocument svgDocument, Element g) {
        Element element = createElement(g.getOwnerDocument(), "ellipse");
        element.setAttribute("cx", "" + svgDocument.origin().getX());
        element.setAttribute("cy", "" + svgDocument.origin().getY());
        element.setAttribute("stroke", svgDocument.stroke());
        element.setAttribute("stroke-width", "1");
        element.setAttribute("fill", "none");

        element.setAttribute("rx", String.valueOf(shape.radiusx().doubleValue()));
        element.setAttribute("ry", String.valueOf(shape.radiusy().doubleValue()));
        g.appendChild(element);



        if (subscribedCircle) {
            g.appendChild(g.getOwnerDocument().createComment("Circumscribed circle of " + shape));
            Element circumscribed = circumscribedCircle(g.getOwnerDocument(), svgDocument, shape);
            g.appendChild(circumscribed);
        }

    }
}
