package org.meeuw.jupiter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.junit.jupiter.api.extension.*;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.jupiter.Rounding;
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
    public void beforeAll(ExtensionContext context) throws Exception {

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        context.getTestClass().ifPresent(testCass -> {
            setRounding(testCass, context);
        });
        context.getTestMethod().ifPresent(testMethod -> {
            setRounding(testMethod, context);
        });
    }

    private void setRounding(AnnotatedElement annotatedElement, ExtensionContext context) {
        Rounding rounding = getAnnotation(annotatedElement);
        if (rounding != null) {
            context.getStore(ns).put("reset", ConfigurationService.setConfiguration(builder -> {
                builder.configure(UncertaintyConfiguration.class, config -> {
                    return config.withExplicitStripZeros(true).withNotation(UncertaintyConfiguration.Notation.ROUND_VALUE);
                });
            }));
        }
    }

    private static Rounding getAnnotation(AnnotatedElement annotatedElement) {
        Rounding rounding = annotatedElement.getAnnotation(Rounding.class);
        if (rounding != null) {
            return rounding;
        }
        for (Annotation parent : annotatedElement.getAnnotations()) {
            rounding = getAnnotation(parent.getClass());
            if (rounding != null) {
                return rounding;
            }
        }
        return null;
    }
}
