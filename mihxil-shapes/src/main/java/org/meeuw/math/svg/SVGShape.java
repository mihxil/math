package org.meeuw.math.svg;

import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.shapes.dim2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public abstract class SVGShape<S extends Shape<?, S>> implements SVGGroup {

    protected final S shape;

    private final boolean circumscribedCircle;

    protected SVGShape(S shape, boolean circumscribedCircle) {
        this.shape = shape;
        this.circumscribedCircle = circumscribedCircle;
    }

    @Override
    public final void fill(SVGDocument svgDocument, Element g) {
        fillShape(svgDocument, g);
        if (circumscribedCircle) {
            g.appendChild(g.getOwnerDocument().createComment("Circumscribed circle of " + shape));
            Element circumscribed = circumscribedCircle( g.getOwnerDocument(), svgDocument, shape);
            g.appendChild(circumscribed);
        }

    }

    abstract void fillShape(SVGDocument svgDocument, Element g);

    protected Element circumscribedCircle(Document doc,SVGDocument svgDocument,  Shape<?, ?> shape) {

        LocatedShape<?, ? extends Circle<?>> circleLocatedShape = shape.circumscribedCircle();

        StringBuilder points = new StringBuilder();
        Circle<?> circle = circleLocatedShape.shape();
        FieldVector2<?> offset = circleLocatedShape.location();
        Element circumscribed = createElement(doc, "circle");
        circumscribed.setAttribute("cx", String.valueOf(svgDocument.origin().getX() + offset.getX().doubleValue()));
        circumscribed.setAttribute("cy", String.valueOf(svgDocument.origin().getY() + offset.getY().doubleValue()));
        circumscribed.setAttribute("r", "" + circle.radius().doubleValue());
        circumscribed.setAttribute("stroke", svgDocument.stroke());
        circumscribed.setAttribute("stroke-opacity", "0.3");
        circumscribed.setAttribute("stroke-dasharray", "1,1");
        circumscribed.setAttribute("stroke-width", "0.2");
        circumscribed.setAttribute("fill", "none");

        return circumscribed;
    }

}
