package io.qameta.htmlelements.locator;

import io.qameta.htmlelements.annotation.*;
import org.openqa.selenium.By;

import java.lang.reflect.Method;
import java.util.*;

import static io.qameta.htmlelements.util.ReflectionUtils.getParameters;

public class Annotations {

    private final Method method;

    private final Object[] args;

    public Annotations(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public By buildBy() {
        FindBy findBy = getMethod().getAnnotation(FindBy.class);
        String locator = findBy.value();

        Map<String, String> parameters = getParameters(method, args);
        for (String key : parameters.keySet()) {
            locator = locator.replaceAll("\\{\\{ " + key + " \\}\\}", parameters.get(key));
        }
        return By.xpath(locator);
    }

}
