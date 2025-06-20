package org.meeuw.math.svg;

import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.shapes.dim2.Polygon;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public class SVGPolygon<F extends CompleteScalarFieldElement<F>, S extends Polygon<F, S>> extends SVGShape<S> {

    @lombok.Builder
    protected SVGPolygon(S polygon, boolean circumscribedCircle, boolean circumscribedRectangle, Consumer<Element> circumscribedCircleAttributes, Consumer<Element> circumscribedRectangleAttributes) {
        super(polygon, circumscribedCircle, circumscribedRectangle, circumscribedCircleAttributes, circumscribedRectangleAttributes);
    }

    @Override
    void fillShape(SVGDocument svgDocument, Element g) {

        Element element = createElement(g.getOwnerDocument(), "polygon");
        g.appendChild(element);

        StringBuilder points = new StringBuilder();
        shape.vertices().forEach(v -> {
            if (points.length() > 0) {
                points.append(" ");
            }
            points.append(v.getX().doubleValue()).append(",").append(v.getY().doubleValue());
        });
        element.setAttribute("points", points.toString());
        element.setAttribute("stroke", svgDocument.stroke());
        element.setAttribute("stroke-width", "1");
        element.setAttribute("fill", "none");

        {
            g.appendChild(g.getOwnerDocument().createComment("Dot showing the first vertex"));

            FieldVector2<?> firstPoint = shape.vertices().findFirst().get();

            Element dot = createElement(g.getOwnerDocument(), "circle");
            dot.setAttribute("cx", String.valueOf(firstPoint.getX().doubleValue()));
            dot.setAttribute("cy", String.valueOf(firstPoint.getY().doubleValue()));
            dot.setAttribute("r", "1.1");
            dot.setAttribute("fill", "#ff0000");
            g.appendChild(dot);
        }
        {
            //g.appendChild(circumscribedCircle(doc, polygon));
        }
    }
    public static class Builder<F extends CompleteScalarFieldElement<F>, S extends Polygon<F, S>>  {

        public Builder<F, S> polygon(S polygon) {
            this.polygon = polygon;
            return this;
        }
    }
}
