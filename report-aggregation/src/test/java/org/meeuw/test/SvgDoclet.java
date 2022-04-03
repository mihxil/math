package org.meeuw.test;

import jdk.javadoc.doclet.*;

import java.util.Locale;
import java.util.Set;

import javax.lang.model.SourceVersion;

public class SvgDoclet implements Doclet {
    @Override
    public void init(Locale locale, Reporter reporter) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return null;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return null;
    }

    @Override
    public boolean run(DocletEnvironment environment) {
        environment.getSpecifiedElements()
            .forEach(e -> {
                System.out.println(e.getSimpleName());
            });
        return true;
    }
}
