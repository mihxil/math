package org.meeuw.math.svg;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.shapes.dim2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

public class SVGRegularPolygon<F extends CompleteScalarFieldElement<F>, S extends RegularPolygon<F>> extends SVGPolygon<F, RegularPolygon<F>> {

    private final boolean inscribedCircle;

    @lombok.Builder(builderMethodName = "regularPolygonBuilder")
    public SVGRegularPolygon(S polygon, boolean circumscribedCircle, boolean inscribedCircle, boolean circumscribedRectangle) {
        super(polygon, circumscribedCircle, circumscribedRectangle);
        this.inscribedCircle = inscribedCircle;
    }

    @Override
    public void fillShape(SVGDocument svgDocument, org.w3c.dom.Element g) {
        super.fillShape(svgDocument, g);
        if (inscribedCircle) {
            g.appendChild(g.getOwnerDocument().createComment("Inscribed circle of " + shape));
            Element inscribed = inscribedCircle(g.getOwnerDocument(), svgDocument, shape);
            g.appendChild(inscribed);
        }
    }

    protected static Element inscribedCircle(Document doc, SVGDocument svgDocument, RegularPolygon<?> shape) {
        Circle<?> circle = shape.inscribedCircle();
        Element inscribed = createElement(doc, "circle");
        inscribed.setAttribute("r", "" + circle.radius().doubleValue());
        inscribed.setAttribute("stroke", svgDocument.stroke());
        inscribed.setAttribute("stroke-opacity", "0.3");
        inscribed.setAttribute("stroke-dasharray", "1,1");
        inscribed.setAttribute("stroke-width", "0.2");
        inscribed.setAttribute("fill", "none");

        doc.getDocumentElement().appendChild(inscribed);

        return inscribed;
    }
}
