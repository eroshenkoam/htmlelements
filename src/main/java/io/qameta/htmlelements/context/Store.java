package io.qameta.htmlelements.context;

import java.util.Optional;

public interface Store {

    String DRIVER_KEY = "driver";
    String BASE_URL_KEY = "baseUrl";

    Optional<Object> get(Object key);

    <T> Optional<T> get(Object key, Class<T> returnType);

    void put(Object key, Object value);

    Optional<Object> remove(Object key);

    <T> Optional<T> remove(Object key, Class<T> returnType);

}
