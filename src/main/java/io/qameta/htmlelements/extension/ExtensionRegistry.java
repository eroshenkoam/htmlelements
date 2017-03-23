package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.annotation.Description;
import io.qameta.htmlelements.util.ReflectionUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExtensionRegistry {

    public static ExtensionRegistry create(Class<?> extensionClass) {
        ExtensionRegistry registry = new ExtensionRegistry();

        ReflectionUtils.getMethods(extensionClass)
                .forEach(method -> Arrays.stream(method.getAnnotations())
                        .filter(annotation -> annotation.annotationType().isAnnotationPresent(ExtendWith.class))
                        .map(annotation -> annotation.annotationType().getAnnotation(ExtendWith.class).value())
                        .forEach(registry::registerExtension));

        Map<String, MethodHandler> handlers = new HashMap<>();
        ReflectionUtils.getMethods(extensionClass).forEach(method -> {
            getHandleWithAnnotation(method).ifPresent(annotation -> {
                Class<? extends MethodHandler> handlerClass = annotation.value();
                MethodHandler handler = ReflectionUtils.newInstance(handlerClass);
                handlers.put(method.getName(), handler);
            });
        });

        registry.getHandlers().putAll(handlers);
        return registry;
    }

    private final Map<Class<? extends Extension>, Extension> extensions;

    private final Map<String, MethodHandler> handlers;

    private ExtensionRegistry() {
        this.extensions = new HashMap<>();
        this.handlers = new HashMap<>();
    }

    public void registerExtension(Class<? extends Extension> extensionType) {
        getExtensions().putIfAbsent(extensionType, ReflectionUtils.newInstance(extensionType));
    }

    public void registerExtensions(AnnotatedElement element) {
        Arrays.stream(element.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(ExtendWith.class))
                .map(annotation -> annotation.annotationType().getAnnotation(ExtendWith.class).value())
                .forEach(this::registerExtension);
    }

    private Map<Class<? extends Extension>, Extension> getExtensions() {
        return extensions;
    }

    public <T extends Extension> List<T> getExtensions(Class<T> extensionType) {
        return getExtensions().values().stream()
                .filter(extension -> extensionType.isAssignableFrom(extension.getClass()))
                .map(extensionType::cast)
                .collect(Collectors.toList());
    }

    private Map<String, MethodHandler> getHandlers() {
        return handlers;
    }

    public Optional<? extends MethodHandler> getHandler(Method method) {
        //TODO: нужно добавть стратегию
        if (method.isDefault()) {
            return Optional.of(new DefaultMethod.Extension());
        }
        return Optional.ofNullable(getHandlers().get(method.getName()));
    }

    private static Optional<HandleWith> getHandleWithAnnotation(AnnotatedElement element) {
        return Arrays.stream(element.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(HandleWith.class))
                .map(annotation -> annotation.annotationType().getAnnotation(HandleWith.class))
                .findFirst();
    }
}
