package io.qameta.htmlelements.extension;

import io.qameta.htmlelements.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private List<Extension> getRegisteredExtensions() {
        return registeredExtensions;
    }

    public <T extends Extension> List<T> getExtensions(Class<T> extensionType) {
        return getRegisteredExtensions().stream()
                .filter(extension -> extensionType.isAssignableFrom(extension.getClass()))
                .map(extensionType::cast)
                .collect(Collectors.toList());

    }

    public void registerExtension(Extension extension) {
        getRegisteredExtensions().add(extension);
    }
}
