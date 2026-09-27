package org.meeuw.jupiter.impl;

import lombok.extern.java.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

import net.jqwik.api.lifecycle.*;
import org.junit.jupiter.api.extension.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.jupiter.WithNumberConfiguration;
import org.meeuw.jupiter.WithUncertaintyConfiguration;
import org.meeuw.math.text.configuration.*;


/**
 * Jupiter extension to set some configuratin aspect for junit tests.
 *
 */
@Log
@WithNumberConfiguration // Serves as default
@WithUncertaintyConfiguration
public class ConfigurationExtension implements
    AfterTestExecutionCallback,
    BeforeTestExecutionCallback,
    AroundPropertyHook
{

    // JUNIT

    private static  final ExtensionContext.Namespace ns = ExtensionContext.Namespace.create(ConfigurationExtension.class);
    private static final String RESET_UNCERTAINTY_CONFIGURATION = "resetUncertaintyConfiguration";
    private static final String RESET_NUMBER_CONFIGURATION = "resetNumberConfiguration";


    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.fine("beforeTestExecution called for: " + context.getDisplayName() + " element=" + context.getElement().map(Object::toString).orElse("<none>"));
        AnnotatedElement[] annotatedArray = relevantAnnotatedElements(
            context.getTestMethod().orElse(null),
            context.getTestClass().orElse(null));

        context.getStore(ns).put(RESET_UNCERTAINTY_CONFIGURATION,
            setUncertaintyConfiguration(annotatedArray)
        );
        context.getStore(ns).put(RESET_NUMBER_CONFIGURATION,
            setNumberConfiguration(annotatedArray)
        );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        // Restore configuration if a reset handle was stored during beforeTestExecution
        log.fine("afterTestExecution called for: " + context.getDisplayName() + " element=" + context.getElement().map(Object::toString).orElse("<none>"));
        for (String key : new String[]{RESET_UNCERTAINTY_CONFIGURATION, RESET_NUMBER_CONFIGURATION}) {
            AutoCloseable reset = (AutoCloseable) context.getStore(ns).remove(key);
            if (reset != null) {
                log.info(key + " -> " + reset);
                reset.close();
            }
        }
    }


    // JQWIK
    @Override
    @NonNull
    public PropertyExecutionResult aroundProperty(@NonNull PropertyLifecycleContext context, PropertyExecutor property) throws Throwable {
        AnnotatedElement[] annotatedArray = relevantAnnotatedElements(context.targetMethod(), context.containerClass());
        try (AutoCloseable resetUncertainty =
                 setUncertaintyConfiguration(annotatedArray);
             AutoCloseable resetNumber =
                 setNumberConfiguration(annotatedArray)
        ) {
            return property.execute();
        }
    }

    @Override
    @NonNull
    public PropagationMode propagateTo() {
        return PropagationMode.ALL_DESCENDANTS;
    }


    // IMPLEMENTATION


    /**
     * method, class, enclosing class(es), package, ConfigurationExtension.class
     * @param m
     * @param containerClass
     * @return
     */

    static AnnotatedElement[] relevantAnnotatedElements(Method m, Class<?> containerClass) {
        List<AnnotatedElement> annotatedElements = new ArrayList<>();
        annotatedElements.add(m);
        Class<?> enclosing = containerClass;
        while (enclosing != null) {
            annotatedElements.add(enclosing);
            enclosing = enclosing.getEnclosingClass();
        }
        if (containerClass != null) {
            annotatedElements.add(containerClass.getPackage());
        }
        annotatedElements.add(ConfigurationExtension.class);
        return annotatedElements.toArray(new AnnotatedElement[0]);
    }

    private static ConfigurationService.Reset setUncertaintyConfiguration(AnnotatedElement... annotatedElements) {
        for (AnnotatedElement annotatedElement : annotatedElements) {
            WithUncertaintyConfiguration setUncertaintyConfiguration = getAnnotation(annotatedElement, WithUncertaintyConfiguration.class);
            log.fine(() -> "applying " + setUncertaintyConfiguration);
            if (setUncertaintyConfiguration != null) {
                return setUncertaintyConfiguration(setUncertaintyConfiguration);
            }
        }
        return null;
    }

    private static ConfigurationService.Reset setUncertaintyConfiguration(WithUncertaintyConfiguration setUncertaintyConfiguration) {
        return ConfigurationService.setConfiguration(builder ->
            builder.configure(UncertaintyConfiguration.class, config ->
                config
                    .withExplicitStripZeros(setUncertaintyConfiguration.stripZeros())
                    .withNotation(setUncertaintyConfiguration.notation())
                    .withWidthOfConfidenceInterval(setUncertaintyConfiguration.widthOfConfidenceInterval())));
    }

    private static ConfigurationService.Reset setNumberConfiguration(AnnotatedElement... annotatedElements) {
        for (AnnotatedElement annotatedElement : annotatedElements) {
            WithNumberConfiguration numberConfiguration = getAnnotation(annotatedElement,
                WithNumberConfiguration.class);
            if (numberConfiguration != null) {
                return setNumberConfiguration(numberConfiguration);
            }
        }
        return null;
    }

    private static ConfigurationService.Reset setNumberConfiguration(WithNumberConfiguration numberConfiguration) {
        return
            ConfigurationService.setConfiguration(builder ->
                builder
                    .configure(NumberConfiguration.class,
                        config -> config.withMaximalPrecision(numberConfiguration.maxPrecision()))
                    .configure(AngleConfiguration.class,
                        ac -> ac.withUnit(numberConfiguration.angles()))
                    .configure("org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberConfiguration", "withMode", numberConfiguration.rationals())
            );
    }

    private static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotation) {
        // Delegate to recursive implementation with a visited set to avoid cycles in meta-annotations
        A a = getAnnotation(
            annotatedElement,
            annotation,
            new HashSet<>());
        if (a == null) {
            //a = getAnnotation(ConfigurationExtension.class, annotation);
            log.fine("applying default " + a);
        }
        return a;
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
