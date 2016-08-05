package io.qameta.htmlelements.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ComplexHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return getMethodCallHandler(method)
                .orElseThrow(() -> new UnsupportedOperationException(
                        String.format("Method '%s' is not implemented", method.getName())))
                .invoke(proxy, method, args);

    }

    private Optional<MethodCallHandler> getMethodCallHandler(Method method) {
        List<MethodCallHandler> handlers = Arrays.asList(
                new DefaultMethodCallHandler(),
                new ToStringMethodCallHandler(),
                new ShouldMatchedMethodCallHandler(),
                new WaitUntilMatchedMethodCallHandler(),
                new FilterMatchedMethodCallHandler(),
                new HtmlElementMethodCallHandler(),
                new HtmlElementListMethodCallHandler()
        );

        return handlers.stream().filter(handler -> handler.canHandle(method)).findFirst();
    }

}
