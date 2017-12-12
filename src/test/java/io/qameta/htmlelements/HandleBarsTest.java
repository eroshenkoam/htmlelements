package io.qameta.htmlelements;

import io.qameta.htmlelements.util.HandlebarsUtils;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * @author nogert <nogert@yandex-team.ru>
 */
public class HandleBarsTest {

    @Test
    public void resolveCorrectTemplate() {
        String original = "{{template}} is correct";
        HashMap<String, String> parameters = new HashMap<String, String>() {{
            put("template", "Template");
        }};

        assertEquals("Template is correct", HandlebarsUtils.resolveVars(original, parameters));
    }

    @Test
    public void resolveCorrectTemplateWithSpaces() {
        String original = "{{ template }} is correct";
        HashMap<String, String> parameters = new HashMap<String, String>() {{
            put("template", "Template");
        }};

        assertEquals("Template is correct", HandlebarsUtils.resolveVars(original, parameters));
    }

    @Test
    public void notResolveNotCorrectTemplate() {
        String original = "{{template is not correct";
        HashMap<String, String> parameters = new HashMap<String, String>() {{
            put("template", "Template");
        }};

        assertEquals("{{template is not correct", HandlebarsUtils.resolveVars(original, parameters));
    }
}
