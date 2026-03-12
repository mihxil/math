package org.meeuw.jupiter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.extension.*;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.jupiter.SetUncertaintyConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

public class ConfigurationExtension implements AfterTestExecutionCallback, BeforeTestExecutionCallback, BeforeAllCallback,
    AfterAllCallback {

    private static  final ExtensionContext.Namespace ns = ExtensionContext.Namespace.create(ConfigurationExtension.class);


    @Override
    public void afterAll(ExtensionContext context) throws Exception {

    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        // Restore configuration if a reset handle was stored during beforeTestExecution
        Object reset = context.getStore(ns).remove("reset");
        if (reset != null) {
            if (reset instanceof AutoCloseable) {
                ((AutoCloseable) reset).close();
            } else if (reset instanceof Runnable) {
                ((Runnable) reset).run();
            }
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        context.getTestClass().ifPresent(testCass -> {
            setUncertaintyConfiguration(testCass, context);
        });
        context.getTestMethod().ifPresent(testMethod -> {
            setUncertaintyConfiguration(testMethod, context);
        });
    }

    private void setUncertaintyConfiguration(AnnotatedElement annotatedElement, ExtensionContext context) {
        SetUncertaintyConfiguration rounding = getAnnotation(annotatedElement, SetUncertaintyConfiguration.class);
        if (rounding != null) {
            context.getStore(ns).put("reset", ConfigurationService.setConfiguration(builder -> {
                builder.configure(UncertaintyConfiguration.class, config ->
                    config.withExplicitStripZeros(rounding.stripZeros())
                        .withNotation(rounding.notation()));
            }));
        }
    }

    private static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotation) {
        // Delegate to recursive implementation with a visited set to avoid cycles in meta-annotations
        return getAnnotation(annotatedElement, annotation, new HashSet<>());
    }

    // Recursive helper that tracks visited AnnotatedElements to prevent infinite loops
    private static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotation, Set<AnnotatedElement> visited) {
        if (annotatedElement == null) {
            return null;
        }
        if (!visited.add(annotatedElement)) {
            return null; // already visited
        }

        // direct annotation present?
        A found = annotatedElement.getAnnotation(annotation);
        if (found != null) {
            return found;
        }

        // otherwise, inspect all annotations present on this element and recurse into their annotation types
        for (Annotation ann : annotatedElement.getAnnotations()) {
            Class<? extends Annotation> annType = ann.annotationType();
            A from = getAnnotation(annType, annotation, visited);
            if (from != null) {
                return from;
            }
        }

        return null;
    }
}
