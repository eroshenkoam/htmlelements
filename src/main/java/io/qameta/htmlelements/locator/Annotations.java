package io.qameta.htmlelements.locator;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.annotation.Param;
import org.openqa.selenium.By;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

public class Annotations {

    private final Method method;

    public Annotations(Method method) {
        this.method = method;
    }

    protected Method getMethod() {
        return method;
    }

    public By buildBy() {
        FindBy findBy = getMethod().getAnnotation(FindBy.class);
        if (method.getParameters().length != 0) {
            Parameter parameter = method.getParameters()[0];
            System.out.println(parameter.getAnnotation(Param.class).value());
        }

        return By.xpath(findBy.value());
    }

    public List<WebElementValidator> getValidators() {
        return Collections.singletonList(
                element -> element.isDisplayed() && element.isEnabled()
        );
    }
}
