package io.qameta.htmlelements.handler;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import static io.qameta.htmlelements.util.ReflectionUtils.getAllMethods;

public class PageObjectHandler extends ComplexHandler {

    private final Supplier<WebDriver> supplier;

    public PageObjectHandler(Supplier<WebDriver> supplier) {
        this.supplier = supplier;
    }

    private Supplier<WebDriver> getSupplier() {
        return supplier;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {

        Class<?> proxyClass = WebDriver.class;

        if (method.getReturnType().isAssignableFrom(proxyClass)) {
            return getSupplier().get();
        }

        if (getAllMethods(proxyClass).contains(method)) {
            return invokeSupplierMethod(getSupplier(), method, args);
        }

        return super.invoke(object, method, args);
    }

    private <T> Object invokeSupplierMethod(Supplier<T> supplier, Method method, Object[] args) throws Throwable {
        return method.invoke(supplier.get(), args);
    }
}
