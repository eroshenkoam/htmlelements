package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.util.ReflectionUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExtensionRegistry {

    public static ExtensionRegistry create(Class<?> extensionClass) {
        List<Extension> extensions = new ArrayList<>();
        ReflectionUtils.getMethods(extensionClass)
                .forEach(method -> Arrays.stream(method.getAnnotations())
                        .filter(annotation -> annotation.annotationType().isAnnotationPresent(ExtendWith.class))
                        .map(annotation -> annotation.annotationType().getAnnotation(ExtendWith.class).value())
                        .map(ReflectionUtils::newInstance)
                        .forEach(extensions::add));

        Map<Method, MethodHandler> handlers = new HashMap<>();
        ReflectionUtils.getMethods(extensionClass).forEach(method -> {
            getHandleWithAnnotation(method).ifPresent(annotation -> {
                Class<? extends MethodHandler> handlerClass = annotation.value();
                MethodHandler handler = ReflectionUtils.newInstance(handlerClass);
                handlers.put(method, handler);
            });
        });

        ExtensionRegistry registry = new ExtensionRegistry();
        registry.getExtensions().addAll(extensions);
        registry.getHandlers().putAll(handlers);
        return registry;
    }

    private final List<Extension> extensions;

    private final Map<Method, MethodHandler> handlers;

    private ExtensionRegistry() {
        this.extensions = new ArrayList<>();
        this.handlers = new HashMap<>();
    }

    private List<Extension> getExtensions() {
        return extensions;
    }

    private Map<Method, MethodHandler> getHandlers() {
        return handlers;
    }

    public <T extends Extension> List<T> getExtensions(Class<T> extensionType) {
        return getExtensions().stream()
                .filter(extension -> extensionType.isAssignableFrom(extension.getClass()))
                .map(extensionType::cast)
                .collect(Collectors.toList());
    }

    public Optional<? extends MethodHandler> getHandler(Method method) {
        return Optional.of(getHandlers().get(method));
    }

    private static Optional<HandleWith> getHandleWithAnnotation(AnnotatedElement element) {
        return Arrays.stream(element.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(HandleWith.class))
                .map(annotation -> annotation.annotationType().getAnnotation(HandleWith.class))
                .findFirst();
    }
}
