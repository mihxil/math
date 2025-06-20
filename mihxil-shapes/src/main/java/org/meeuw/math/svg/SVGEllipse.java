package org.meeuw.math.svg;

import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.shapes.dim2.Ellipse;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public class SVGEllipse<F extends CompleteScalarFieldElement<F>> extends SVGShape<Ellipse<F>> {


    private final boolean subscribedCircle;

    @lombok.Builder
    private SVGEllipse(Ellipse<F> ellipse, boolean circumscribedCircle, boolean circumscribedRectangle, Consumer<Element> circumscribedCircleAttributes, Consumer<Element> circumscribedRectangleAttributes) {
        super(ellipse, circumscribedCircle, circumscribedRectangle, circumscribedCircleAttributes, circumscribedRectangleAttributes);
        this.subscribedCircle = circumscribedCircle;
    }

    @Override
    public void fillShape(SVGDocument svgDocument, Element g) {
        Element element = createElement(g.getOwnerDocument(), "ellipse");
        element.setAttribute("stroke", svgDocument.stroke());
        element.setAttribute("stroke-width", "1");
        element.setAttribute("fill", "none");

        element.setAttribute("rx", String.valueOf(shape.radiusx().doubleValue()));
        element.setAttribute("ry", String.valueOf(shape.radiusy().doubleValue()));
        if (shape.angle().doubleValue() != 0) {
            element.setAttribute("transform", "rotate(" + Math.toDegrees(shape.angle().doubleValue()) + ")");
        }


        g.appendChild(element);



        if (subscribedCircle) {
            g.appendChild(g.getOwnerDocument().createComment("Circumscribed circle of " + shape));
            Element circumscribed = circumscribedCircle(g.getOwnerDocument(), svgDocument, shape);
            g.appendChild(circumscribed);
        }

    }
}
