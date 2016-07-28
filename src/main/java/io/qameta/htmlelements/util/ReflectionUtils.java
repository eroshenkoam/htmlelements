package io.qameta.htmlelements.util;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.List;

import static java.util.Arrays.stream;

public class ReflectionUtils {

    public static List<Class<?>> getAllInterfaces(Class<?> clazz) {
        List<Class<?>> result = ClassUtils.getAllInterfaces(clazz);
        result.add(clazz);
        return result;
    }

    public static List<Method> getAllMethods(Class<?> clazz) {
        return getAllInterfaces(clazz).stream()
                .flatMap(m -> stream(m.getDeclaredMethods()))
                .collect(Collectors.toList());
    }
}
