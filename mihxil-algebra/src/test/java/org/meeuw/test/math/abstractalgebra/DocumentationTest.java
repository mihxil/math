/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.abstractalgebra;

import lombok.With;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import org.meeuw.math.Example;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.categoryofgroups.Element;
import org.meeuw.math.operators.*;
import org.meeuw.math.text.TextUtils;
import org.reflections.Reflections;

import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;


/**
 * This is not testing much. It uses introspection to create a file 'algebras.dot.m4', which will be (by a github action)
 * converted to the SVG in the documentation
 */
@SuppressWarnings({"TextBlockMigration", "unchecked"})
@Log4j2
public class DocumentationTest {
    final Reflections reflections = new Reflections(AlgebraicStructure.class.getPackageName());

    public static String ALGEBRA_URL = "ALGEBRA_URL";
    public static String MATH_URL    = "MATH_URL";

    public static String BINARY_OPERATOR_COLOR = "teal";
    public static String COMPARISON_COLOR = "blue";
    public static String SPECIAL_COLOR = "navy";


    @Test
    public void dot() throws IOException {
        File dest = new File(System.getProperty("user.dir"), "../docs/algebras.dot.m4");
        try (OutputStream outputStream = new FileOutputStream(dest)) {
            dot(outputStream);
        }
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void showAll() {
        Set<Class<? extends AlgebraicStructure>> subTypes = reflections.getSubTypesOf(AlgebraicStructure.class);

        subTypes.forEach(c -> {
            if ((c.getModifiers() & Modifier.PUBLIC) != 0 && !c.isInterface() && (c.getModifiers() & Modifier.ABSTRACT) == 0) {
                //log.info(c.getSimpleName() + "->");
            }
        });

    }

    @SuppressWarnings("rawtypes")
    public void dot(OutputStream out) {
        Set<Class<? extends AlgebraicStructure>> subTypes =
            reflections.getSubTypesOf(AlgebraicStructure.class);
        subTypes.add(AlgebraicStructure.class);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        digraph(writer, (w) ->
            subTypes.forEach(c -> {
                if ((c.getModifiers() & Modifier.PUBLIC) != 0 && c.isInterface()) {
                    try {
                        writeInterface(writer, c);
                    } catch (Throwable e) {
                        log.error(e.getMessage());
                    }
                }
                //log.info("{}", c);
            })
        );
        writer.close();
    }

    String getModule(Class<?> c) {
        URL location = c.getResource('/' + c.getName().replace('.', '/') + ".class");
        assert location != null;
        return Arrays.stream(location.toString().split("/")).filter(s -> s.matches("mihxil-[a-z]*")).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    protected <C> C proxy(Class<C> interfac) {
        final C c =  (C) Proxy.newProxyInstance(DocumentationTest.class.getClassLoader(), new Class[]{interfac},
            (proxy, method, args) -> {
                if (method.isDefault()) {
                    // if it's a default method, invoke it
                    return InvocationHandler.invokeDefault(proxy, method, args);
                } else if ("toString".equals(method.getName())) {
                    return "proxy for " + interfac;
                } else {
                    return null;
                }
            });
        log.debug("Proxying {}: {}", interfac, c);
        return c;

    }
    protected <C> Stream<Class<? extends C>> getExamplesClasses(Class<C> interfac) {
        return reflections.getSubTypesOf(interfac).stream()
            .filter(
                c -> Arrays.stream(c.getAnnotationsByType(Example.class))
                    .anyMatch(e ->
                        e.value().equals(interfac)
                    )
            );
    }
    protected <C> Stream<C> getExamplesConstants(Class<C> interfac) {
        return reflections.getSubTypesOf(interfac).stream()
            .flatMap(
                c -> Arrays.stream(c.getDeclaredFields())
                    .filter(f -> Arrays.stream(f.getAnnotationsByType(Example.class)).anyMatch(e -> e.value().equals(interfac)))
                    .filter(f -> Modifier.isStatic(f.getModifiers()))
                    .map(f -> {
                        try {
                            return (C) f.get(null);
                        } catch (IllegalAccessException e) {
                            log.error(e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
            );
    }


    protected <C extends AlgebraicStructure<?>> void writeLabel(PrintWriter writer, Class<C> c, int colspan, Consumer<PrintWriter> body) {
        if (colspan == 0) {
            colspan = 1;
        }
        writer.println("\t\tmargin=2\tlabel=<");
        writer.println("<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>");
        writeCaption(writer,(w) -> w.write(toString(c)), colspan, href(c), c.getSimpleName());
        body.accept(writer);
        writer.println("</table>");
        writer.println(">");

    }
    protected <C extends AlgebraicStructure<?>>  void writeExamples(final PrintWriter writer, Class<C> target, int cols)  {
        getExamplesClasses(target)
            .forEach(t ->
                writeCaption(writer, p -> p.write(toString(t)), cols, href(t), t.getSimpleName())
            );

        getExamplesConstants(target).forEach(s ->
            writeCaption(writer, p -> p.write(s.toString()), cols, href(s.getClass()), s.getDescription())
        );

    }

    protected void writeCaption(PrintWriter writer, Consumer<PrintWriter> body, int cols, String href, String title) {
        writer.write("<tr><td colspan='" + cols + "'");
        if (href != null) {
            writer.write(" title='" + title + "' href='" + href + "'");
        }
        writer.write(">");
        if (href != null) {
            writer.write("<font color='#0000a0'>");
        }
        body.accept(writer);
        if (href != null) {
            writer.write("</font>");
        }
        writer.write("</td></tr>");

    }

    protected <C extends AlgebraicStructure<?>> String toString(Class<C> structureClass) {
        StringBuilder build = new StringBuilder();
        Example a = structureClass.getAnnotation(Example.class);
        if (a != null && ! a.prefix().equals("")) {
            build.append(a.prefix());
        }
        if (a != null && ! a.string().equals("")) {
            build.append(a.string());
        } else {
            build.append(structureClass.getSimpleName());
        }

        try {
            Field instance = structureClass.getDeclaredField("INSTANCE");
            C c = (C) instance.get(null);
            if (! build.toString().equals(c.toString())) {
                build.append(' ').append(c);
            }
        } catch (NoSuchFieldException ignored) {

        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
        return build.toString();
    }

    Map<String, String> specialSpecials = Map
        .of("zero", "0",
            "one", "1",
            "unity", "u",
            "pi", "\uD835\uDF0B",
            "e", "ℯ",
            "getCardinality", ""
        );


    protected <C extends AlgebraicStructure<?>>  List<OperatorCell> getOperators(Class<C> clazz, C target) {
        final List<OperatorCell> ops = new ArrayList<>();
        if (target.getSupportedOperators().contains(OPERATION)) {
            ops.add(new OperatorCell(OPERATION.stringify("", "")).withTitle("group binary operator"));

        }
        {
            StringBuilder addition = new StringBuilder();
            if (target.getSupportedOperators().contains(ADDITION)) {
                addition.append(ADDITION.getSymbol());
            }
            if (target.getSupportedOperators().contains(SUBTRACTION)) {
                addition.append(SUBTRACTION.getSymbol());
            }
            if (target instanceof AdditiveSemiGroup) {
                if (((AdditiveSemiGroup<?>) target).additionIsCommutative()) {
                    addition.append("\n⇆");
                }
            }
            if (!addition.isEmpty()) {
                ops.add(new OperatorCell(addition).withTitle("binary operators of addition"));
            }
        }
        {
            StringBuilder multiplication = new StringBuilder();
            if (target.getSupportedOperators().contains(MULTIPLICATION)) {
                multiplication.append(MULTIPLICATION.getSymbol());
            }
            if (target.getSupportedOperators().contains(DIVISION)) {
                multiplication.append(DIVISION.getSymbol());
            }
            if (target instanceof MultiplicativeSemiGroup) {
                if (((MultiplicativeSemiGroup<?>) target).multiplicationIsCommutative()) {
                    multiplication.append("\n⇆");
                }
            }
            if (!multiplication.isEmpty()) {
                ops.add(new OperatorCell(multiplication).withTitle("binary operators of multiplication"));
            }
        }

        {
            StringBuilder rest = new StringBuilder();
            for (AlgebraicBinaryOperator o : target.getSupportedOperators()) {
                if (o.ordinal() > DIVISION.ordinal()) {
                    if (!rest.isEmpty()) {
                        rest.append(' ');
                    }
                    rest.append(o.stringify("", ""));
                }
            }
            for (AlgebraicComparisonOperator o : target.getSupportedComparisonOperators()) {
                if (!rest.isEmpty()) {
                    rest.append(' ');
                }
                rest.append(o.stringify("", ""));
            }
            if (!rest.isEmpty()) {
                ops.add(new OperatorCell(rest).withTitle("other binary operators"));
            }
        }
        {
            StringBuilder unary = new StringBuilder();
            for (AlgebraicUnaryOperator o : target.getSupportedUnaryOperators()) {
                if (! unary.isEmpty()) {
                    unary.append(" ");
                }
                unary.append(o.getSymbol());
            }

            for (GenericFunction o : target.getSupportedFunctions()) {
                if (! unary.isEmpty()) {
                    unary.append(" ");
                }
                unary.append(o.getSymbol());
            }
            if (!unary.isEmpty()) {
                ops.add(new OperatorCell(unary.toString().replaceAll("\\(" + TextUtils.PLACEHOLDER + "\\)", "")).withTitle("Unary operators"));
            }
        }

        {
            List<String> special = new ArrayList<>();
            appendSpecials(special, clazz);

            Collections.reverse(special);
            if (!special.isEmpty()) {
                ops.add(new OperatorCell(String.join(" ", special)).withTitle("special elements"));
            }

        }
        return ops;
    }

    protected void appendSpecials(List<String> builder, Class<?> target) {
         for (Method m : target.getDeclaredMethods()) {
             if (m.getParameterTypes().length == 0) {
                 if (AlgebraicElement.class.isAssignableFrom(m.getReturnType()) && ! AlgebraicStructure.class.isAssignableFrom(m.getReturnType())) {
                     String name = specialSpecials.getOrDefault(m.getName(), m.getName());
                     if (name.length() > 0) {
                         if (!builder.contains(name)) {
                             builder.add(name);
                         }
                     }
                 }
             }
         }
         if (target.getSuperclass() != null) {
             appendSpecials(builder, target.getSuperclass());
         }
         for (Class<?> i : target.getInterfaces()) {
             if (! Element.class.equals(i)) {
                 appendSpecials(builder, i);
             }
         }
    }

    protected  int writeOperators(final PrintWriter writer, List<OperatorCell> ops)  {

        if (ops.size() > 0) {
            writer.print("<tr>");
            for (OperatorCell o : ops) {
                writer.print("<td");
                if (o.title != null) {
                    writer.print(" title='" + o.title + "'");
                    writer.print(" href=''");
                }
                if (o.bgColor != null) {
                    writer.print(" bgColor='" + o.bgColor + "'");
                }
                writer.print(">");
                writer.print(o.text);
                writer.print("</td>");
            }
            writer.print("</tr>");
        }
        return ops.size();
    }

    @lombok.AllArgsConstructor
    @lombok.Builder
    protected static class OperatorCell {
        final String text;
        @With
        final String bgColor;

        @With
        final String title;

        public OperatorCell(CharSequence text) {
            this(text.toString()
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\n", "<br />")
                , null, null);
        }

    }

    protected <C extends AlgebraicStructure<?>>  String href(Class<C> c){
        String baseurl;
        String module = getModule(c);
        if ("mihxil-math".equals(module)) {
            baseurl = MATH_URL;
        } else if ("mihxil-algebra".equals(module)){
            baseurl = ALGEBRA_URL;
        } else {
            throw new IllegalArgumentException("Unrecognized module " + module + " for " + c);
        }
        return baseurl + "/" + c.getName().replace(".", "/") + ".java";
    }

    static class Super {
        final String name;
        final boolean pseudo;

        Super(String name, boolean style) {
            this.name = name;
            this.pseudo = style;
        }
    }


    protected <C extends AlgebraicStructure<?>> void writeInterface(final PrintWriter writer, Class<C> c) {
        writer.println("\n\n# " + c);
        writer.println(c.getSimpleName() + "[");
        //writer.println("href=\"" + href(c) + "\"");
        C target = proxy(c);
        List<OperatorCell> operators = getOperators(c, target);
        writeLabel(writer, c, operators.size(), (p) -> {
            int cols = writeOperators(p, operators);
            writeExamples(p, c, cols);
        });

        writer.println("]");


        final Set<Super> supers = new LinkedHashSet<>();
        Stream.concat(Stream.of(c.getSuperclass()), Stream.of(c.getInterfaces())).forEach(superInterface -> {
            if (superInterface != null) {
                if (AlgebraicStructure.class.isAssignableFrom(superInterface)) {
                    boolean pseudo = false;
                    Class<? extends AlgebraicElement<?>> elementClass = getElementClass(c);
                    Class<? extends AlgebraicElement<?>> superElementClass = getElementClass(superInterface);

                    for (Method superMethod : superElementClass.getMethods()) {
                        try {
                            if (superMethod.getParameterTypes().length > 0) {
                                if (! AlgebraicElement.class.isAssignableFrom(superMethod.getParameterTypes()[0])) {
                                    continue;
                                }
                            }
                            if (superMethod.getAnnotation(NonAlgebraic.class) != null) {
                                continue;
                            }

                            Method method = elementClass.getDeclaredMethod(superMethod.getName(), elementClass);
                            log.debug("super: {}.{} -> sub {}.{}", superElementClass.getSimpleName(), superMethod.getName(), elementClass, method.getName());
                            if (method.getAnnotation(NonAlgebraic.class) != null) {
                                pseudo = true;
                                log.info("**** {}.{} is non algebraic -> {} (from {})", elementClass.getSimpleName(), method.getName(), elementClass, superElementClass.getSimpleName());
                            } else if (method.getExceptionTypes().length  > 0) {
                                pseudo = true;
                                log.info("*** {}.{} has exception, and hence cannot be algebraic -> {} (from {})", elementClass.getSimpleName(), method.getName(), elementClass, superElementClass.getSimpleName());
                            }

                        } catch (NoSuchMethodException e) {
                            log.debug(e.getMessage());
                        }
                    }
                    supers.add(new Super(superInterface.getSimpleName(), pseudo));
                }
            }
            }
        );

        String regular = supers.stream().filter(s -> !s.pseudo).map(s -> s.name).collect(Collectors.joining("\n"));
        if (!regular.isEmpty()) {
            writer.println(c.getSimpleName() + " -> {" + regular+ "}");
        }
        String pseudo = supers.stream().filter(s -> s.pseudo).map(s -> s.name).collect(Collectors.joining("\n"));
        if (! pseudo.isEmpty()) {
            writer.println(c.getSimpleName() + " -> {" + pseudo + "}[style=\"dashed\"]");
        }
    }

    protected Class<? extends AlgebraicElement<?>> getElementClass(Class<?> structureClass) {
        for (TypeVariable<?> p : structureClass.getTypeParameters()) {
            for(Type t : p.getBounds()) {
                Class<?> c = (Class<?>) ((ParameterizedType) t).getRawType();
                if (AlgebraicElement.class.isAssignableFrom(c)) {
                    return (Class<? extends AlgebraicElement<?>>) c;
                }

            }

        }
        return null;
    }

    protected void digraph(PrintWriter writer, Consumer<Writer> body) {
        writer.write("digraph {\n" +
            "    node [\n" +
            "\t\t  shape=plain\n" +
            "    ]\n" +
            "\t\tedge [\n" +
            "\t\t  arrowhead = \"empty\"\n" +
            "\t\t]\n\n");
        writer.println("        define(`" + MATH_URL + "', https://github.com/mihxil/math/blob/main/mihxil-math/src/main/java)");
        writer.println("        define(`" + ALGEBRA_URL + "', https://github.com/mihxil/math/blob/main/mihxil-algebra/src/main/java)");
        writer.println("         changecom(`  #')\n"); // don't match css color
        body.accept(writer);
        writer.write("}\n");
    }
}


