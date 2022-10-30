/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
