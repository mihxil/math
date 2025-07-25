package org.meeuw.math.svg;


import java.util.Arrays;

import org.meeuw.math.abstractalgebra.dim2.Vector2;
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
        Vector2 origin = svg.origin();


        {
            String minusY = String.valueOf(gridSize.height().doubleValue() / -2);
            String plusY = String.valueOf(gridSize.height().doubleValue() / 2);


            Element yaxis = verticalLine(g, "0", minusY, plusY);
            double x = 0d;

            while (x < gridSize.width().doubleValue() / 2) {
                x += spacing.width().doubleValue();
                Arrays.asList(x, -x).forEach(i -> {
                    Element line = verticalLine(g, String.valueOf( i),
                        minusY, plusY);
                    line.setAttribute("stroke-dasharray", "1,1");
                });
            }
        }
        {
            String minusX = String.valueOf(gridSize.width().doubleValue() / -2);
            String plusX = String.valueOf(gridSize.width().doubleValue() / 2);
            Element xaxis = horizontalLine(g,
                minusX,
                plusX,
                "0");
            double y = 0d;
            while (y < gridSize.height().doubleValue()) {
                y += spacing.height().doubleValue();

                Arrays.asList(y, -y).forEach(i -> {
                    Element line = horizontalLine(g, minusX,
                        plusX,
                        String.valueOf( i));
                    line.setAttribute("stroke-dasharray", "1,1");
                });
            }
        }


    }

    private Element line(Document doc) {
        Element line = createElement(doc, "line");
        line.setAttribute("stroke", "#00ff00");
        line.setAttribute("stroke-width", "0.2");
        return line;
    }
    private Element verticalLine(Element g, String x, String y1, String y2) {
        Element line = line(g.getOwnerDocument());
        line.setAttribute("x1", x);
        line.setAttribute("y1", y1);
        line.setAttribute("x2", x);
        line.setAttribute("y2", y2);
        g.appendChild(line);
        return line;
    }
    private Element horizontalLine(Element g, String x1, String x2, String y) {
        Element line = line(g.getOwnerDocument());
        line.setAttribute("x1", x1);
        line.setAttribute("y1", y);
        line.setAttribute("x2", x2);
        line.setAttribute("y2", y);
        g.appendChild(line);
        return line;
    }

}
