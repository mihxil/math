package org.meeuw.math.abstractalgebra.polynomial;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.text.TextUtils;

class Parser {


    static <E extends AbelianRingElement<E>> Polynomial<E> fromString(PolynomialRing<E> ring, String input) {
        input = input
            .replaceAll("\\s+", "")
            .replaceAll("-", "+-");
        input = TextUtils.unsuperscript(input);
        if (input.startsWith("+")) {
            input = input.substring(1);
        }

        AbelianRing<E> coefficientRing = ring.getCoefficientRing();
        String[] terms = input.split("\\+");
        Map<Integer, E> coeffs = new HashMap<>();

        Pattern termPattern = Pattern.compile("(.*?)·?(?:" + ring.getVariable() + "\\^?(\\d*))?");
        for (String term : terms) {
            if (term.isEmpty()) continue;
            Matcher m = termPattern.matcher(term);
            if (m.matches()) {
                String coeff = m.group(1);
                String e = m.group(2);
                final int exp;
                if (e == null) {
                    exp = 0;
                } else {
                    if (e.isEmpty()) {
                        exp = 1;
                    } else {
                        exp = Integer.parseInt(e);
                    }
                }
                E coeffValue;
                if (coeff.isEmpty()) {
                    coeffValue = coefficientRing.one();
                } else if (coeff.equals("-")) {
                    coeffValue = coefficientRing.one().negation();
                } else {
                    coeffValue = coefficientRing.fromString(coeff);
                }
                E prev = coeffs.get(exp);
                if (prev == null) {
                    coeffs.put(exp, coeffValue);
                } else {
                    coeffs.put(exp, prev.plus(coeffValue));
                }

            } else {
                throw new NotParsable("Cannot parse term: " + term);
            }
        }
        int maxExp = coeffs.keySet().stream().max(Integer::compareTo).orElse(0);
        if (maxExp == 0 &&  coeffs.getOrDefault(0, coefficientRing.zero()).isZero()) {
            return ring.zero();
        }
        E[] arr = coefficientRing.newArray(maxExp + 1);
        for (int i = 0; i <= maxExp; i++) {
            arr[i] = coeffs.getOrDefault(i, coefficientRing.zero());
        }
        return new Polynomial<>(ring, arr);
    }
}
