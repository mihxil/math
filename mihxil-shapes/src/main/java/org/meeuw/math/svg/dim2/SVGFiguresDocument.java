package org.meeuw.math.svg.dim2;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;
import org.meeuw.math.shapes.Shape;
import org.meeuw.math.shapes.dim2.*;
import org.meeuw.math.svg.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuperBuilder
public class SVGFiguresDocument <
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>>
    extends SVGDocument<E, C> {

    @Getter
    private final Rectangle<E, C> size;

    @Getter
    private final FieldVector2<E, C> origin;

    private SVGFiguresDocument(
        String stroke,
        float textSize,
        Rectangle<E, C> size,
        FieldVector2<E, C> origin,
        List<? extends SVGGroup<E, C>> groups,
        List<? extends Shape<E, C, ?, ?>> shapes
        ) {
        super(stroke, textSize, groups, shapes);
        this.size = size;
        this.origin = origin;
    }

    public SVGFiguresDocument<E, C> withSize(Rectangle<E, C> size) {
        return new SVGFiguresDocument<>(stroke, textSize, size, origin, groups, shapes);
    }

    public SVGFiguresDocument<E, C> withOrigin(FieldVector2<E, C> origin) {
        return new SVGFiguresDocument<>(stroke, textSize, size, origin, groups, shapes);
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
        parentG.setAttribute("transform",  "translate(" + origin.getX() + "," + origin.getY() + ")");
        document.getDocumentElement().appendChild(parentG);
        for (SVGGroup<E, C> group : groups) {
            group.accept(this, parentG);
        }

        return document;
    }

    public SVGFiguresDocument<E, C> addGrid(Consumer<SVGGrid.Builder<E, C>> gridConsumer) {
        SVGGrid.Builder<E, C> gridBuilder = SVGGrid.<E, C>builder();
        gridConsumer.accept(gridBuilder);
        add(null, gridBuilder.build());
        return this;
    }

    public SVGFiguresDocument<E, C> addGrid() {
        return addGrid((builder) -> {});
    }

    public  SVGFiguresDocument<E, C> addInfo(
            Consumer<SVGInfo.Builder<E, C>> infoConsumer) {
        SVGInfo.Builder<E, C> infoBuilder = SVGInfo.<E, C>builder();
        infoConsumer.accept(infoBuilder);
        add(null, infoBuilder.build());
        return this;
    }

    public  SVGFiguresDocument<E, C> addInfo() {
        return addInfo((builder) -> {});
    }



    public  SVGFiguresDocument<E, C> addPolygon(Polygon<E, C> polygon, Consumer<SVGPolygon.Builder<E, C, ?>> polygonConsumer) {
        SVGPolygon.Builder<E, C, Polygon<E, C>> polygonBuilder = SVGPolygon.<E, C, Polygon<E, C>>builder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygon, polygonBuilder.build());
        return this;
    }
    public SVGFiguresDocument<E, C> addRegularPolygon(RegularPolygon<E, C> polygon, Consumer<SVGRegularPolygon.RegularPolygonBuilder<E, C>> polygonConsumer) {

        SVGRegularPolygon.RegularPolygonBuilder<E, C> polygonBuilder = SVGRegularPolygon.<E, C>regularPolygonBuilder();
        polygonBuilder.polygon(polygon);
        polygonConsumer.accept(polygonBuilder);
        add(polygon, polygonBuilder.build());
        return this;
    }

    public SVGFiguresDocument<E, C> addCircle(Circle<E, C> circle) {
        return addCircle(circle, (builder) -> {
        });
    }


    public  SVGFiguresDocument<E, C> addCircle(Circle<E, C> circle, Consumer<SVGCircle.Builder<E, C>> circleConsumer) {
        SVGCircle.Builder<E, C> builder = SVGCircle.<E, C>builder();
        builder.circle(circle);
        circleConsumer.accept(builder);
        add(circle, builder.build());
        return this;
    }

    public SVGFiguresDocument<E, C> addEllipse(Ellipse<E, C> ellipse, Consumer<SVGEllipse.Builder<E, C>> ellipseConsumer) {
        SVGEllipse.Builder<E, C> ellipseBuilder = SVGEllipse.<E, C>builder();
        ellipseBuilder.ellipse(ellipse);
        ellipseConsumer.accept(ellipseBuilder);
        add(ellipse, ellipseBuilder.build());
        return this;
    }


}
