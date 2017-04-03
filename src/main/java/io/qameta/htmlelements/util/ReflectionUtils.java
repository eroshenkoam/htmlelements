package io.qameta.htmlelements.util;

import com.google.common.base.Joiner;
import io.qameta.htmlelements.annotation.Description;
import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.annotation.Param;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.By;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class ReflectionUtils {

    private static final int UNIQ_LOCATOR_COUNT = 1;


    public static <T> T newInstance(Class<T> clazz) {
        try {
            return ConstructorUtils.invokeConstructor(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Class<?>> getAllInterfaces(Class<?>[] classes) {
        List<Class<?>> result = new ArrayList<>();
        Arrays.stream(classes).forEach(clazz -> {
            result.addAll(ClassUtils.getAllInterfaces(clazz));
            result.add(clazz);
        });
        return result;
    }

    public static List<Method> getMethods(Class<?>... clazz) {

        return getAllInterfaces(clazz).stream()
                .flatMap(m -> stream(m.getDeclaredMethods()))
                .collect(Collectors.toList());
    }

    public static List<String> getMethodsNames(Class<?>[] clazz, String... additional) {
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

    public static String getDescription(Class<?> clazz) {
        return splitCamelCase(clazz.getSimpleName());
    }

    public static By getSelector(Method method, Object[] args) {
        Function<String,String> replaceParam= locator->{
            Map<String, String> parameters = getParameters(method, args);
            for (String key : parameters.keySet()) {
                locator = locator.replaceAll("\\{\\{" + key + "\\}\\}", parameters.get(key));
            }
            return locator;

        };

        By by= null;
        String locator;
        FindBy findBy=method.getAnnotation(FindBy.class);
        List<String> locators = new ArrayList<>();
        //xpath
        if(!findBy.xpath().isEmpty()) {
            locator=replaceParam.apply(findBy.xpath());
            by = By.xpath(locator);
            locators.add(locator);
        }
        //css
        if(!findBy.css().isEmpty()) {
            locator=replaceParam.apply(findBy.css());
            by = By.cssSelector(locator);
            locators.add(locator);
        }
        //id
        if(!findBy.id().isEmpty()) {
            locator=replaceParam.apply(findBy.id());
            by = By.id(locator);
            locators.add(locator);
        }

        if (locators.size() != UNIQ_LOCATOR_COUNT) {
            throw new IllegalArgumentException(
                    String.format("You must specify at most one location strategy. Number found: %d (%s)",
                            locators.size(), locators.toString()));
        }
        return by;
    }

    public static String getDescription(Method method, Object[] args) {
        if (method.isAnnotationPresent(Description.class)) {
            Map<String, String> parameters = getParameters(method, args);
            String name = method.getAnnotation(Description.class).value();
            for (String key : parameters.keySet()) {
                name = name.replaceAll("\\{\\{" + key + "\\}\\}", parameters.get(key));
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
