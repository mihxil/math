package org.meeuw.math.svg;

import lombok.Getter;
import lombok.With;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.abstractalgebra.integers.ModuloFieldElement;
import org.meeuw.math.shapes.dim2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@lombok.Builder
public class SVGDocument {

    @Getter
    @lombok.Builder.Default
    @With
    private final Rectangle<ModuloFieldElement> size = Rectangle.of(200, 200);

    @With
    private final Vector2 origin;

    @Getter
    @lombok.Builder.Default
    @With
    private final String stroke = "black";

    @Getter
    @lombok.Builder.Default
    @With
    private final float textSize = 4;

    @lombok.Builder.Default
    private final List<SVGGroup> groups = new ArrayList<>();

    @Getter
    @lombok.Builder.Default
    private final List<Shape<?, ?>> shapes = new ArrayList<>();


    @lombok.Builder
    private SVGDocument(
        Rectangle<ModuloFieldElement> size,
        Vector2 origin,
        String stroke,
        float textSize,
        List<SVGGroup> groups,
        List<Shape<?, ?>> shapes
        ) {
        this.size = size;
        this.origin = origin;
        this.stroke = stroke;
        this.textSize = textSize;
        this.groups = groups;
        this.shapes = shapes;
    }

    public Vector2 origin() {
        return origin == null ?  new Vector2(size.width().doubleValue()/ 2, size.height().doubleValue() / 2) : origin; // the center of the : origin;
    }

    public static SVGDocument defaultSVG() {
        return builder().build();
    }

     /**
     * Creates a DOM Document with the SVG root element and all groups added to this document.
     */
    public Document buildDocument() {
        Document document = SVG.DOCUMENT_BUILDER.newDocument();
        Element root = document.createElementNS(SVG.SVG_NAMESPACE, "svg");
        root.setAttribute("width", String.valueOf(size.width().doubleValue()));
        root.setAttribute("height", String.valueOf(size.height().doubleValue()));
        document.appendChild(root);
        Element parentG = SVG.createElement(document, "g");
        parentG.setAttribute("transform",  "translate(" + origin().getX() + "," + origin().getY() + ")");
        document.getDocumentElement().appendChild(parentG);
        for (SVGGroup group : groups) {
            group.accept(this, parentG);
        }

        return document;
    }

    protected void add(Shape<?, ?> shape, SVGGroup svgGroup) {
        groups.add(svgGroup);
        if (shape != null) {
            shapes.add(shape);
        }

    }

    public SVGDocument addGrid(Consumer<SVGGrid.Builder> gridConsumer) {
        SVGGrid.Builder gridBuilder = SVGGrid.builder();
        gridConsumer.accept(gridBuilder);
        add(null, gridBuilder.build());
        return this;
    }
    public SVGDocument addGrid() {
        return addGrid((builder) -> {});
    }
    public  <F extends CompleteScalarFieldElement<F>, S extends Shape<F, S>> SVGDocument addInfo() {
        add(null, new SVGInfo());
        return this;
    }

    public <F extends CompleteScalarFieldElement<F>, S extends Polygon<F, S>> SVGDocument addPolygon(S polygon, Consumer<SVGPolygon.Builder<F, S>> polygonConsumer) {
        SVGPolygon.Builder<F, S> polygonBuilder = SVGPolygon.builder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygon, polygonBuilder.build());
        return this;
    }
    public  <F extends CompleteScalarFieldElement<F>, S extends RegularPolygon<F>> SVGDocument addRegularPolygon(S polygon, Consumer<SVGRegularPolygon.Builder<F, S>> polygonConsumer) {

        SVGRegularPolygon.Builder<F, S> polygonBuilder = SVGRegularPolygon
            .regularPolygonBuilder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygon, polygonBuilder.build());
        return this;
    }

    public <F extends CompleteScalarFieldElement<F>> SVGDocument addCircle(Circle<F> circle) {
        return addCircle(circle, (builder) -> {
        });
    }


    public <F extends CompleteScalarFieldElement<F>> SVGDocument addCircle(Circle<F> circle, Consumer<SVGCircle.Builder<F>> circleConsumer) {
        SVGCircle.Builder<F> builder = SVGCircle.builder();
        builder.circle(circle);
        circleConsumer.accept(builder);
        add(circle, builder.build());
        return this;
    }

    public <F extends CompleteScalarFieldElement<F>> SVGDocument addEllipse(Ellipse<F> ellipse, Consumer<SVGEllipse.Builder<?>> ellipseConsumer) {
        SVGEllipse.Builder<F> ellipseBuilder = SVGEllipse.builder();
        ellipseBuilder.ellipse(ellipse);
        ellipseConsumer.accept(ellipseBuilder);
        add(ellipse, ellipseBuilder.build());
        return this;
    }


}
