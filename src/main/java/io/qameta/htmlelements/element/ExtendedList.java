package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.ConvertMethod;
import io.qameta.htmlelements.extension.DefaultMethod;
import io.qameta.htmlelements.extension.DescriptionProvider;
import io.qameta.htmlelements.extension.FilterMethod;
import io.qameta.htmlelements.extension.SelectorProvider;
import io.qameta.htmlelements.extension.ShouldMethod;
import io.qameta.htmlelements.extension.WaitUntilMethod;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ExtendedList<ItemType> extends List<ItemType> {

    @SelectorProvider
    String getSelector();

    @DescriptionProvider
    String getDescription();

    @FilterMethod
    ExtendedList<ItemType> filter(Predicate<ItemType> predicate);

    @ConvertMethod
    <R> ExtendedList<R> convert(Function<ItemType, R> function);

    @ShouldMethod
    ExtendedList<ItemType> should(Matcher matcher);

    @WaitUntilMethod
    ExtendedList<ItemType> waitUntil(Predicate<ExtendedList<ItemType>> predicate);

    @DefaultMethod
    default ExtendedList<ItemType> filter(String description, Predicate<ItemType> predicate) {
        return filter(predicate);
    }

    @DefaultMethod
    default ExtendedList<ItemType> filter(Matcher matcher) {
        return filter(matcher::matches);
    }

    @DefaultMethod
    default ExtendedList<ItemType> waitUntil(String description, Predicate<ExtendedList<ItemType>> predicate) {
        return waitUntil(predicate);
    }

    @DefaultMethod
    default ExtendedList<ItemType> waitUntil(Matcher matcher) {
        return waitUntil(matcher.toString(), matcher::matches);
    }

}
