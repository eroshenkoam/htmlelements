package io.qameta.htmlelements.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultStore implements Store {

    private final Map<Object, Object> storage;

    public DefaultStore() {
        this.storage = new HashMap<>();
    }

    @Override
    public Optional<Object> get(Object key) {
        return get(key, Object.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Object key, Class<T> returnType) {
        Object value = getStorage().get(key);
        return Optional.ofNullable((T) value);
    }

    @Override
    public void put(Object key, Object value) {
        getStorage().put(key, value);
    }

    @Override
    public Optional<Object> remove(Object key) {
        return remove(key, Object.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> remove(Object key, Class<T> returnType) {
        return Optional.ofNullable((T) getStorage().remove(key));
    }

    private Map<Object, Object> getStorage() {
        return storage;
    }
}
