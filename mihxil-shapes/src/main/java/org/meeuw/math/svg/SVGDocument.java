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

    @Getter
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

    private final List<SVGGroup> groups = new ArrayList<>();


    @lombok.Builder
    private SVGDocument(
        Rectangle<ModuloFieldElement> size,
        Vector2 origin,
        String stroke,
        float textSize
        ) {
        this.size = size;
        this.origin = origin == null ?  new Vector2(size.width().doubleValue()/ 2, size.height().doubleValue() / 2) : origin; // the center of the : origin;
        this.stroke = stroke;
        this.textSize = textSize;
    }

    public void add(SVGGroup svgGroup) {
        groups.add(svgGroup);
    }


    public SVGDocument addGrid(Consumer<SVGGrid.Builder> gridConsumer) {
        SVGGrid.Builder gridBuilder = SVGGrid.builder();
        gridConsumer.accept(gridBuilder);
        add(gridBuilder.build());
        return this;
    }
    public SVGDocument addGrid() {
        return addGrid((builder) -> {});
    }
    public  <F extends CompleteScalarFieldElement<F>, S extends Shape<F, S>> SVGDocument addInfo(Shape<F, S> shape) {
        add(new SVGInfo(shape));
        return this;
    }

    public <F extends CompleteScalarFieldElement<F>, S extends Polygon<F, S>> SVGDocument addPolygon(S polygon, Consumer<SVGPolygon.Builder<F, S>> polygonConsumer) {
        SVGPolygon.Builder<F, S> polygonBuilder = SVGPolygon.builder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygonBuilder.build());
        return this;
    }
    public  <F extends CompleteScalarFieldElement<F>, S extends RegularPolygon<F>> SVGDocument addRegularPolygon(S polygon, Consumer<SVGRegularPolygon.Builder<F, S>> polygonConsumer) {

        SVGRegularPolygon.Builder<F, S> polygonBuilder = SVGRegularPolygon
            .regularPolygonBuilder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygonBuilder.build());
        return this;
    }

    public <F extends CompleteScalarFieldElement<F>> SVGDocument addCircle(Circle<F> circle, Consumer<SVGCircle.Builder<F>> circleConsumer) {
        SVGCircle.Builder<F> builder = SVGCircle.builder();
        builder.circle(circle);
        circleConsumer.accept(builder);
        add(builder.build());
        return this;
    }

    public <F extends CompleteScalarFieldElement<F>> SVGDocument addEllipse(Ellipse<F> ellipse, Consumer<SVGEllipse.Builder<?>> ellipseConsumer) {
        SVGEllipse.Builder<F> ellipseBuilder = SVGEllipse.builder();
        ellipseBuilder.ellipse(ellipse);
        ellipseConsumer.accept(ellipseBuilder);
        add(ellipseBuilder.build());
        return this;
      }

    public Document document() {
        Document document = SVG.DOCUMENT_BUILDER.newDocument();
        Element root = document.createElementNS(SVG.SVG_NAMESPACE, "svg");
        root.setAttribute("width", String.valueOf(size.width().doubleValue()));
        root.setAttribute("height", String.valueOf(size.height().doubleValue()));
        document.appendChild(root);
        for (SVGGroup group : groups) {
            group.accept(this, document);
        }
        return document;
    }
}
