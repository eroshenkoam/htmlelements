package io.qameta.htmlelements.util;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.util.Map;

/**
 * @author nogert <nogert@yandex-team.ru>
 */
public class HandlebarsUtils {

    private static Handlebars handlebars = new Handlebars();

    private HandlebarsUtils() {
    }

    public static String resolveVars(String original, Map<String, String> parameters) {
        try {
            Template template = handlebars.compileInline(original);
            original = template.apply(parameters);
        } catch (Exception ignored) {
            //do nothing
        }
        return original;
    }
}
