package io.qameta.htmlelements.util;

import com.google.common.base.Joiner;
import io.qameta.htmlelements.annotation.Description;
import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.annotation.Name;
import io.qameta.htmlelements.annotation.Param;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class ReflectionUtils {

    private static List<Class<?>> getAllInterfaces(Class<?> clazz) {
        List<Class<?>> result = ClassUtils.getAllInterfaces(clazz);
        result.add(clazz);
        return result;
    }

    public static List<String> getMethods(Class<?> clazz, String... additional) {
        return Stream.concat(
                Arrays.stream(additional),
                getAllInterfaces(clazz).stream()
                        .flatMap(m -> stream(m.getDeclaredMethods()))
                        .map(Method::getName)
        ).collect(Collectors.toList());
    }

    public static Map<String, String> getParameters(Method method, Object[] args) {
        Map<String, String> parameters = new HashMap<>();
        if (args == null) {
            return parameters;
        }

        for (int i = 0; i < args.length; i++) {
            parameters.put(method.getParameters()[i].getAnnotation(Param.class).value(), args[i].toString());
        }
        return parameters;
    }

    public static String getName(Class<?> clazz) {
        return splitCamelCase(clazz.getSimpleName());
    }

    public static String getSelector(Method method, Object[] args) {
        Map<String, String> parameters = getParameters(method, args);
        String selector = method.getAnnotation(FindBy.class).value();
        for (String key : parameters.keySet()) {
            selector = selector.replaceAll("\\{\\{ " + key + " \\}\\}", parameters.get(key));
        }
        return selector;
    }

    public static String getName(Method method, Object[] args) {
        if (method.isAnnotationPresent(Description.class)) {
            Map<String, String> parameters = getParameters(method, args);
            String name = method.getAnnotation(Description.class).value();
            for (String key : parameters.keySet()) {
                name = name.replaceAll("\\{\\{ " + key + " \\}\\}", parameters.get(key));
            }
            return name;
        } else {
            return splitCamelCase(method.getName());
        }
    }

    private static String splitCamelCase(String text) {
        return Joiner.on(" ").join(StringUtils.splitByCharacterTypeCamelCase(text));
    }

}
