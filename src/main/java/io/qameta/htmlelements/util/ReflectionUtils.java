package io.qameta.htmlelements.util;

import io.qameta.htmlelements.annotation.Param;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

}
