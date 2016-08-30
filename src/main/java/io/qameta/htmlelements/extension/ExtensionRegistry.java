package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.util.ReflectionUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        return new ExtensionRegistry(extensions);
    }

    private final List<Extension> registeredExtensions = new ArrayList<>();

    private ExtensionRegistry(List<Extension> extensions) {
        getRegisteredExtensions().addAll(extensions);
    }

    public List<Extension> getRegisteredExtensions() {
        return registeredExtensions;
    }

    public <T extends Extension> List<T> getExtensions(Class<T> extensionType) {
        return getRegisteredExtensions().stream()
                .filter(extension -> extensionType.isAssignableFrom(extension.getClass()))
                .map(extensionType::cast)
                .collect(Collectors.toList());
    }

    public Optional<? extends MethodHandler> getHandler(Method method) {
        return getHandleWithAnnotation(method).map(handleWith -> ReflectionUtils.newInstance(handleWith.value()));
    }

    private Optional<HandleWith> getHandleWithAnnotation(AnnotatedElement element) {
        return Arrays.stream(element.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(HandleWith.class))
                .map(annotation -> annotation.annotationType().getAnnotation(HandleWith.class))
                .findFirst();
    }
}
