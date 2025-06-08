package org.meeuw.math.svg;


import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.shapes.dim2.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;

@lombok.Builder
public class SVGGrid implements SVGGroup {

    @lombok.Builder.Default
    private  final Rectangle<ModuloFieldElement> spacing = Rectangle.of(10, 10);

    @lombok.Builder
    private SVGGrid(Rectangle<ModuloFieldElement> spacing) {
        this.spacing = spacing;

    }

    @Override
    public void fill(SVGDocument svg, Element g) {
        Rectangle<ModuloFieldElement> gridSize = svg.size();
        g.setAttribute("id", "grid");
        Document doc = g.getOwnerDocument();
        g.appendChild(doc.createComment("Grid"));
        for (int i = 0; i < gridSize.width().intValue(); i += spacing.width().intValue()) {
            Element line = createElement(doc, "line");
            line.setAttribute("x1", String.valueOf(i));
            line.setAttribute("y1", "0");
            line.setAttribute("x2", String.valueOf(i));
            line.setAttribute("y2", gridSize.height().intValue() + "");
            line.setAttribute("stroke", "#00ff00");
            line.setAttribute("stroke-width", "0.2");
            if (i != 100) {
                line.setAttribute("stroke-dasharray", "1,1");
            }
            g.appendChild(line);
        }
        for (int i = 0; i < gridSize.height().intValue(); i += spacing.height().intValue()) {
            Element line = createElement(doc, "line");
            line.setAttribute("y1", String.valueOf(i));
            line.setAttribute("x1", "0");
            line.setAttribute("y2", String.valueOf(i));
            line.setAttribute("x2", gridSize.width().intValue() + "");
            line.setAttribute("stroke", "#00ff00");
                line.setAttribute("stroke-width", "0.2");
                if (i != 100) {
                    line.setAttribute("stroke-dasharray", "1,1");
                }
                g.appendChild(line);
        }

    }

}
