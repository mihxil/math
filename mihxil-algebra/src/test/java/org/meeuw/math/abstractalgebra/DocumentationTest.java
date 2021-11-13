package org.meeuw.math.abstractalgebra;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import org.meeuw.math.Example;
import org.reflections.Reflections;

@Log4j2
public class DocumentationTest {
    final Reflections reflections = new Reflections(AlgebraicStructure.class.getPackageName());


    @Test
    public void dot() throws IOException {
        File dest = new File(System.getProperty("user.dir"), "../docs/algebras.dot.m4");
        try (OutputStream outputStream = new FileOutputStream(dest)) {
            dot(outputStream);
        }
    }

    @SuppressWarnings("rawtypes")
    public void dot(OutputStream out) throws IOException {
        Set<Class<? extends AlgebraicStructure>> subTypes = reflections.getSubTypesOf(AlgebraicStructure.class);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        digraph(writer, (w) -> {
            subTypes.forEach(c -> {
                if ((c.getModifiers() & Modifier.PUBLIC) != 0 && c.isInterface()) {
                    try {
                        writeInterface(writer, c);
                    } catch (Throwable e) {
                        log.error(e.getMessage());
                    }
                }
                //log.info("{}", c);
            });
        });
        writer.close();


    }
    @SuppressWarnings("unchecked")
    protected <C> C proxy(Class<C> interfac) {
        log.info("Proxying {}", interfac);
        return (C) Proxy.newProxyInstance(DocumentationTest.class.getClassLoader(), new Class[]{interfac},
            (proxy, method, args) -> {
                if (method.isDefault()) {
                    // if it's a default method, invoke it
                    return InvocationHandler.invokeDefault(proxy, method, args);
                } else {
                    return null;
                }
            });

    }
    protected <C> Stream<Class<? extends C>> getExamples(Class<C> interfac) {
        return reflections.getSubTypesOf(interfac).stream()
            .filter(
                c -> Arrays.stream(c.getAnnotationsByType(Example.class))
                    .anyMatch(e ->
                        e.value().equals(interfac)
                    )
            );
    }

    protected void writeLabel(PrintWriter writer, Class<?> c, Runnable runnable) {
        writer.print("\t\tlabel=\"{" + c.getSimpleName() + "|");
        runnable.run();
        writer.println("}\"");

    }
    protected <C extends AlgebraicStructure<?>>  void writeExamples(final PrintWriter writer, Class<C> target)  {
        String example = getExamples(target).map(Class::getSimpleName).collect(Collectors.joining("\\n"));
        if (! example.isEmpty()) {
            writer.write("|" + example);
        }
    }

    protected <C extends AlgebraicStructure<?>>  void writeOperators(final PrintWriter writer, C target)  {
        List<String> ops = new ArrayList<>();
        {
            StringBuilder addition = new StringBuilder();
            if (target.getSupportedOperators().contains(Operator.ADDITION)) {
                addition.append(" + ");
            }
            if (target.getSupportedOperators().contains(Operator.SUBTRACTION)) {
                addition.append(" - ");
            }
            if (target instanceof AdditiveSemiGroup) {
                if (((AdditiveSemiGroup<?>) target).additionIsCommutative()) {
                    addition.append("\\n⇆");
                }
            }
            if (!addition.isEmpty()) {
                ops.add(addition.toString());
            }
        }
        {
            StringBuilder multiplication = new StringBuilder();
            if (target.getSupportedOperators().contains(Operator.MULTIPLICATION)) {
                multiplication.append(" * ");
            }
            if (target.getSupportedOperators().contains(Operator.DIVISION)) {
                multiplication.append(" / ");
            }
            if (target instanceof MultiplicativeSemiGroup) {
                if (((MultiplicativeSemiGroup<?>) target).multiplicationIsCommutative()) {
                    multiplication.append("\\n⇆");
                }
            }
            if (!multiplication.isEmpty()) {
                ops.add(multiplication.toString());
            }
        }

        try {
            target.getClass().getMethod("zero");
            ops.add("0");
        } catch (NoSuchMethodException ignored) {
        }
        try {
            target.getClass().getMethod("one");
            ops.add("1");
        } catch (NoSuchMethodException ignored) {
        }
        writer.print("{" + String.join(" | ", ops) + "}");
    }

    protected <C extends AlgebraicStructure<?>> void writeInterface(final PrintWriter writer, Class<C> c) throws Throwable {
        writer.println("\n\n# " + c);
        writer.println(c.getSimpleName() + "[");
        writer.println("href=\"URL\"");
        writeLabel(writer, c, () -> {
            C target = proxy(c);
            writeOperators(writer, target);
            writeExamples(writer, c);
        });

        writer.println("]");



        Set<String> supers = new HashSet<>();
        Stream.concat(Stream.of(c.getSuperclass()), Stream.of(c.getInterfaces())).forEach(sup -> {
            if (sup != null) {
                if (AlgebraicStructure.class.isAssignableFrom(sup)) {
                    supers.add(sup.getSimpleName());
                }
            }
            }
        );
        writer.println(c.getSimpleName() + " -> {" + supers.stream().collect(Collectors.joining("\n")) + "}");
    }

    protected void digraph(PrintWriter writer, Consumer<Writer> body) throws IOException {
        writer.write("digraph {\n" +
            "    node [\n" +
            "\t\t  shape=record\n" +
            "    ]\n" +
            "\t\tedge [\n" +
            "\t\t  arrowhead = \"empty\"\n" +
            "\t\t]\n\n");
        writer.println("        define(URL, https://github.com/mihxil/math/blob/main/mihxil-math/src/main/java/)");
        body.accept(writer);
        writer.write("}\n");
    }
}


