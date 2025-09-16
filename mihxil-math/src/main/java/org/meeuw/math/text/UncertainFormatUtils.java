package org.meeuw.math.text;

import java.text.*;

import org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;

/**
 * @since 0.19
 */
public class UncertainFormatUtils {


    static int parseInt(String value, ParsePosition parsePosition) {
        int pos = parsePosition.getIndex();
        while (isWhitespace(value.charAt(pos))) {
            pos++;
        }
        int i = pos;
        while (i < value.length() && isDigit(value.charAt(i))) {
            i++;
        }
        parsePosition.setIndex(i);
        return Integer.parseInt(value.substring(pos, i));
    }

    public static String valuePlusMinError(String value, String error) {
        boolean empty = error.replaceAll("^[0.]+", "").isEmpty();
        if (empty) {
            return value;
        } else {
            return value + ' ' + TextUtils.PLUSMIN + ' ' + error;
        }
    }
    /**
     * @since 0.19
     */
    static void valuePlusMinError(StringBuffer appendable, Format format, FieldPosition position, Object value, Object error) {
        format.format(value, appendable, position);
        appendable.append(' ');
        appendable.append(TextUtils.PLUSMIN);
        appendable.append(' ');
        final int before = appendable.length();
        format.format(error, appendable, position);
         // remove error indication again if it contains only zeros
        int i = before;
        while (i < appendable.length() && (appendable.charAt(i) == '0' || appendable.charAt(i) == '.')) {
            i++;
        }
        if (i == appendable.length()) {
            appendable.delete(before - 3, i);
        }
    }

    static String valueParenthesesError(String value, String error) {
        int i = 0;
        while (i < error.length() && (error.charAt(i) == '0' || error.charAt(i) == '.')) {
            i++;
        }
        String e = error.substring(i);
        if (e.isEmpty()) {
            return value;
        } else {
            return value + "(" + e + ")";
        }
    }

    /**
     * @since 0.19
     */
    static void valueParenthesesError(StringBuffer appendable, Format format, FieldPosition pos, Object value, Object error) {
        format.format(value, appendable, pos);
        appendable.append('(');
        final int before = appendable.length();
        format.format(error, appendable, pos);
        // remove error indication again if it contains only zerors
        int i = before;
        while (i < appendable.length() && (appendable.charAt(i) == '0' || appendable.charAt(i) == '.')) {
            i++;
        }
        if (i == appendable.length()) {
            appendable.delete(before - 1, i);
        } else {
            appendable.delete(before, i);
            appendable.append(')');
        }

    }

    public static String valueAndError(
        String value, String error,
        Notation uncertaintyNotation) {
        return switch (uncertaintyNotation) {
            case PARENTHESES -> valueParenthesesError(value, error);
            case PLUS_MINUS -> UncertainFormatUtils.valuePlusMinError(value, error);
            case ROUND_VALUE -> value;
            case ROUND_VALUE_AND_TRIM -> value;
        };
    }


    /**
     */
    public static void valueAndError(
        StringBuffer appendable,
        Format format,
        FieldPosition position,
        Object value, Object error,
        Notation uncertaintyNotation) {
        switch (uncertaintyNotation) {
            case PARENTHESES -> valueParenthesesError(appendable, format, position, value, error);
            case PLUS_MINUS -> valuePlusMinError(appendable, format, position, value, error);
            case ROUND_VALUE -> format.format(value, appendable, position);
            case ROUND_VALUE_AND_TRIM -> format.format(value, appendable, position);
        }
    }
}
