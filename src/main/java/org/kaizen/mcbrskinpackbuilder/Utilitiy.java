package org.kaizen.mcbrskinpackbuilder;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author shane.whitehead
 */
public class Utilitiy {
    public static String toProperCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(" "))
                .map(word -> word.isEmpty()
                ? word
                : Character.toTitleCase(word.charAt(0)) + word
                .substring(1)
                .toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
