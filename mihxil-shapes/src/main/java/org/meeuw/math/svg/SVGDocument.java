package org.meeuw.math.svg;

import lombok.Getter;
import lombok.With;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.shapes.dim2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@lombok.Builder
public class SVGDocument {

    @Getter
    @lombok.Builder.Default
    @With
    private final Rectangle<RationalNumber, BigDecimalElement> size = Rectangle.of(200, 200);

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
    private final List<Shape<?, ?, ?>> shapes = new ArrayList<>();


    @lombok.Builder
    private SVGDocument(
        Rectangle<RationalNumber, BigDecimalElement> size,
        Vector2 origin,
        String stroke,
        float textSize,
        List<SVGGroup> groups,
        List<Shape<?, ?, ?>> shapes
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

    protected void add(Shape<?, ?, ?> shape, SVGGroup svgGroup) {
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
    public  <F extends CompleteScalarFieldElement<F>, S extends Shape<F, F, S>> SVGDocument addInfo(Consumer<SVGInfo.Builder> infoConsumer) {
        SVGInfo.Builder infoBuilder = SVGInfo.builder();
        infoConsumer.accept(infoBuilder);
        add(null, infoBuilder.build());
        return this;
    }
    public  <F extends CompleteScalarFieldElement<F>, S extends Shape<F, F, S>> SVGDocument addInfo() {
        return addInfo((builder) -> {});
    }

    public <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>, S extends Polygon<E, C, S>> SVGDocument addPolygon(S polygon, Consumer<SVGPolygon.Builder<E, C, S>> polygonConsumer) {
        SVGPolygon.Builder<E, C, S> polygonBuilder = SVGPolygon.<E, C, S>builder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygon, polygonBuilder.build());
        return this;
    }
    public  <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>, S extends RegularPolygon<E, C>> SVGDocument addRegularPolygon(S polygon, Consumer<SVGRegularPolygon.Builder<E, C, S>> polygonConsumer) {

        SVGRegularPolygon.Builder<E, C, S> polygonBuilder = SVGRegularPolygon.<E, C, S>regularPolygonBuilder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygon, polygonBuilder.build());
        return this;
    }

    public <F extends CompleteScalarFieldElement<F>> SVGDocument addCircle(Circle<F, F> circle) {
        return addCircle(circle, (builder) -> {
        });
    }


    public <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> SVGDocument addCircle(Circle<E, C> circle, Consumer<SVGCircle.Builder<E, C>> circleConsumer) {
        SVGCircle.Builder<E, C> builder = SVGCircle.<E, C>builder();
        builder.circle(circle);
        circleConsumer.accept(builder);
        add(circle, builder.build());
        return this;
    }

    public <E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> SVGDocument addEllipse(Ellipse<E, C> ellipse, Consumer<SVGEllipse.Builder<E, C>> ellipseConsumer) {
        SVGEllipse.Builder<E, C> ellipseBuilder = SVGEllipse.<E, C>builder();
        ellipseBuilder.ellipse(ellipse);
        ellipseConsumer.accept(ellipseBuilder);
        add(ellipse, ellipseBuilder.build());
        return this;
    }


}
