package org.meeuw.math.svg;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.shapes.Shape;
import org.meeuw.math.svg.dim2.SVGFiguresDocument;
import org.meeuw.math.svg.dim3.SVGSolidsDocument;
import org.w3c.dom.Document;

@SuperBuilder
public abstract class SVGDocument<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>> {

    @Getter
    @lombok.Builder.Default
    protected final String stroke = "black";

    @Getter
    @lombok.Builder.Default
    protected final float textSize = 4;

    @Getter
    protected final List<SVGGroup<E, C>> groups = new ArrayList<>();

    @Getter
    protected final List<Shape<E, C, ?, ?>> shapes = new ArrayList<>();


    protected SVGDocument(
        String stroke,
        float textSize,
        List<? extends SVGGroup<E, C>> groups,
        List<? extends Shape<E, C, ?, ?>> shapes
        ) {
        this.stroke = stroke;
        this.textSize = textSize;
        this.groups.addAll(groups);
        this.shapes.addAll(shapes);
    }

    public static SVGFiguresDocument<RationalNumber, BigDecimalElement> default2DDocument() {
        return SVGFiguresDocument.<RationalNumber, BigDecimalElement>builder().build();
    }

    public static SVGSolidsDocument<RationalNumber, BigDecimalElement> default3DDocument() {
        return SVGSolidsDocument.<RationalNumber, BigDecimalElement>builder().build();
    }


    abstract public Document buildDocument();


    protected void add(Shape<E, C, ?, ?> shape, SVGGroup<E, C> svgGroup) {
        groups.add(svgGroup);
        if (shape != null) {
            shapes.add(shape);
        }

    }

    public SVGDocument<E, C> addInfo(Consumer<SVGInfo.Builder<E, C>> infoConsumer) {
        SVGInfo.Builder<E, C> infoBuilder = SVGInfo.<E, C>builder();
        infoConsumer.accept(infoBuilder);
        add(null, infoBuilder.build());
        return this;
    }

    public  SVGDocument<E, C> addInfo() {
        return addInfo((builder) -> {});
    }

}
