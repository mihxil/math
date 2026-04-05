package org.meeuw.jupiter.impl;

import lombok.extern.java.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import net.jqwik.api.lifecycle.*;
import org.junit.jupiter.api.extension.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.jupiter.SetNumberConfiguration;
import org.meeuw.jupiter.SetUncertaintyConfiguration;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.uncertainnumbers.ConfidenceIntervalConfiguration;

@Log
public class ConfigurationExtension implements
    AfterTestExecutionCallback,
    BeforeTestExecutionCallback,
    BeforeAllCallback,
    AfterAllCallback,
    AroundPropertyHook
{

    private static  final ExtensionContext.Namespace ns = ExtensionContext.Namespace.create(ConfigurationExtension.class);
    private static final String RESET_UNCERTAINTY_CONFIGURATION = "resetUncertaintyConfiguration";
    private static final String RESET_NUMBER_CONFIGURATION = "resetNumberConfiguration";


    @Override
    public void afterAll(ExtensionContext context) {
    }




    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        // Restore configuration if a reset handle was stored during beforeTestExecution
        log.fine("afterTestExecution called for: " + context.getDisplayName() + " element=" + context.getElement().map(Object::toString).orElse("<none>"));
        for (String key : new String[]{RESET_UNCERTAINTY_CONFIGURATION, RESET_NUMBER_CONFIGURATION}) {
            Object reset = context.getStore(ns).remove(key);
            if (reset != null) {
                log.info(key + " -> " + reset);
                if (reset instanceof AutoCloseable) {
                    ((AutoCloseable) reset).close();
                } else if (reset instanceof Runnable) {
                    ((Runnable) reset).run();
                }
            }
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) {

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.fine("beforeTestExecution called for: " + context.getDisplayName() + " element=" + context.getElement().map(Object::toString).orElse("<none>"));


        Method m = context.getTestMethod().orElse(null);
        Class<?> clazz = context.getTestClass().orElse(null);

        context.getStore(ns).put(RESET_UNCERTAINTY_CONFIGURATION,
            setUncertaintyConfiguration(m, clazz)
        );
        context.getStore(ns).put(RESET_NUMBER_CONFIGURATION,
            setNumberConfiguration(m, clazz)
        );

    }



    @Override
    public PropertyExecutionResult aroundProperty(PropertyLifecycleContext context, PropertyExecutor property) throws Throwable {
        try (AutoCloseable resetUncertainty =
                 setUncertaintyConfiguration(context.targetMethod(), context.containerClass())) {
            return property.execute();
        }
    }


    private ConfigurationService.Reset setUncertaintyConfiguration(AnnotatedElement... annotatedElements) {
        for (AnnotatedElement annotatedElement : annotatedElements) {
            SetUncertaintyConfiguration setUncertaintyConfiguration = getAnnotation(annotatedElement, SetUncertaintyConfiguration.class);
            if (setUncertaintyConfiguration != null) {
                log.info("applying " + setUncertaintyConfiguration);
                return
                    ConfigurationService.setConfiguration(builder -> {
                        builder.configure(UncertaintyConfiguration.class, config ->
                            config
                                .withExplicitStripZeros(setUncertaintyConfiguration.stripZeros())
                                .withNotation(setUncertaintyConfiguration.notation()));
                        builder.configure(ConfidenceIntervalConfiguration.class, config ->
                            config
                                .withSds(setUncertaintyConfiguration.sds()));
                    });
            }
        }
        return null;
    }

    private ConfigurationService.Reset setNumberConfiguration(AnnotatedElement... annotatedElements) {
        for (AnnotatedElement annotatedElement : annotatedElements) {

            SetNumberConfiguration numberConfiguration = getAnnotation(annotatedElement,
                SetNumberConfiguration.class);
            if (numberConfiguration != null) {
                log.info("applying " + numberConfiguration);
                return
                    ConfigurationService.setConfiguration(builder -> {
                        builder.configure(NumberConfiguration.class, config ->
                            config
                                .withMaximalPrecision(numberConfiguration.maxPrecision()));
                    });
            }
        }
        return null;
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
    @Override
    @NonNull
    public PropagationMode propagateTo() {
        return PropagationMode.ALL_DESCENDANTS;
    }


}
