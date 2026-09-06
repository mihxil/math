package org.meeuw.math.svg.dim3;

import lombok.Getter;
import lombok.With;

import java.util.List;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim3.FieldVector3;
import org.meeuw.math.shapes.dim3.RectangularCuboid;
import org.meeuw.math.shapes.dim3.Solid;
import org.meeuw.math.svg.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@lombok.Builder
public class SVGSolidsDocument<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>
    > extends SVGDocument<E, C> {

    @Getter
    @With
    private final RectangularCuboid<E, C> size;

    @Getter
    @With
    private final FieldVector3<E, C> origin;


    private SVGSolidsDocument(
        String stroke,
        float textSize,
        List<SVGGroup<E, C>> groups,
        List<Solid<E, C>> shapes,
        RectangularCuboid<E, C> size,
        FieldVector3<E, C>  origin
        ) {
        super(stroke, textSize, groups, shapes);
        this.size = size;
        this.origin = origin == null ?  size.asVector().dividedBy(2) :origin;

    }
    /**
     * Creates a DOM Document with the SVG root element and all groups added to this document.
     */
    @Override
    public Document buildDocument() {
        Document document = SVG.DOCUMENT_BUILDER.newDocument();
        Element root = document.createElementNS(SVG.SVG_NAMESPACE, "svg");

        root.setAttribute("width", String.valueOf(size.width().doubleValue()));
        root.setAttribute("height", String.valueOf(size.height().doubleValue()));
        document.appendChild(root);
        Element parentG = SVG.createElement(document, "g");
        parentG.setAttribute("transform",  "translate(" + origin().getX() + "," + origin().getY() + ")");
        document.getDocumentElement().appendChild(parentG);
        for (SVGGroup<E, C> group : groups) {
            group.accept(this, parentG);
        }

        return document;
    }

    protected void add(Solid<E, C> shape, SVGGroup<E, C> svgGroup) {
        groups.add(svgGroup);
        if (shape != null) {
            shapes.add(shape);
        }

    }
/*
    public SVGSolidsDocument<E, C> addGrid(Consumer<SVGGrid.Builder> gridConsumer) {
        SVGGrid.Builder gridBuilder = SVGGrid.builder();
        gridConsumer.accept(gridBuilder);
        add(null, gridBuilder.build());
        return this;
    }

    public SVGSolidsDocument<E, C> addGrid() {
        return addGrid((builder) -> {});
    }

 */

}
