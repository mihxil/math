package org.meeuw.math.svg;

import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.shapes.dim2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public abstract class SVGShape<S extends Shape<?, S>> implements SVGGroup {

    protected final S shape;


    private final boolean circumscribedCircle;
    private final boolean circumscribedRectangle;

    protected SVGShape(S shape, boolean circumscribedCircle, boolean circumscribedRectangle) {
        this.shape = shape;
        this.circumscribedCircle = circumscribedCircle;
        this.circumscribedRectangle = circumscribedRectangle;
    }



    @Override
    public final void fill(SVGDocument svgDocument, Element g) {
        fillShape(svgDocument, g);
        if (circumscribedCircle) {
            g.appendChild(g.getOwnerDocument().createComment("Circumscribed circle of " + shape));
            Element circumscribed = circumscribedCircle( g.getOwnerDocument(), svgDocument, shape);
            g.appendChild(circumscribed);
        }
        if (circumscribedRectangle) {
            g.appendChild(g.getOwnerDocument().createComment("Circumscribed rectangle of " + shape));
            Element circumscribed = circumscribedRectangle( g.getOwnerDocument(), svgDocument, shape);
            // TODO:
            //g.appendChild(circumscribed);
        }

    }

    abstract void fillShape(SVGDocument svgDocument, Element g);

    protected Element circumscribedCircle(Document doc,SVGDocument svgDocument,  Shape<?, ?> shape) {

        LocatedShape<?, ? extends Circle<?>> circleLocatedShape = shape.circumscribedCircle();

        StringBuilder points = new StringBuilder();
        Circle<?> circle = circleLocatedShape.shape();
        FieldVector2<?> offset = circleLocatedShape.location();
        Element circumscribed = createElement(doc, "circle");
        circumscribed.setAttribute("r", "" + circle.radius().doubleValue());
        circumscribed.setAttribute("stroke", svgDocument.stroke());
        circumscribed.setAttribute("stroke-opacity", "0.3");
        circumscribed.setAttribute("stroke-dasharray", "1,1");
        circumscribed.setAttribute("stroke-width", "0.2");
        circumscribed.setAttribute("fill", "none");

        return circumscribed;
    }

     protected Element circumscribedRectangle(Document doc,SVGDocument svgDocument,  Shape<?, ?> shape) {

        LocatedShape<?, ? extends Rectangle<?>> circumscribedRectangle = shape.circumscribedRectangle();

        StringBuilder points = new StringBuilder();
        Rectangle<?> rect = circumscribedRectangle.shape();
        FieldVector2<?> offset = circumscribedRectangle.location();
        Element circumscribed = createElement(doc, "rect");
        circumscribed.setAttribute("x", "" + (rect.width().doubleValue() / -2 + offset.getX().doubleValue()));
        circumscribed.setAttribute("y", "" + (rect.height().doubleValue() / 2 + offset.getY().doubleValue()));
        circumscribed.setAttribute("height", "" + rect.width().doubleValue());
        circumscribed.setAttribute("width", "" + rect.height().doubleValue());
        circumscribed.setAttribute("stroke", svgDocument.stroke());
        circumscribed.setAttribute("stroke-opacity", "0.3");
        circumscribed.setAttribute("stroke-dasharray", "1,1");
        circumscribed.setAttribute("stroke-width", "0.2");
        circumscribed.setAttribute("fill", "none");

        return circumscribed;
    }

}
