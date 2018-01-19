package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.ConvertMethod;
import io.qameta.htmlelements.extension.DescriptionProvider;
import io.qameta.htmlelements.extension.FilterMethod;
import io.qameta.htmlelements.extension.SelectorProvider;
import io.qameta.htmlelements.extension.elements.ShouldMethod;
import io.qameta.htmlelements.extension.Timeout;
import io.qameta.htmlelements.extension.elements.ToStringMethod;
import io.qameta.htmlelements.extension.elements.WaitUntilMethod;
import io.qameta.htmlelements.matcher.PredicateMatcher;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public interface ExtendedList<ItemType> extends List<ItemType> {

    @SelectorProvider
    String getSelector();

    @DescriptionProvider
    String getDescription();

    @ConvertMethod
    <R> ExtendedList<R> convert(Function<ItemType, R> function);

    @FilterMethod
    ExtendedList<ItemType> filter(Predicate<ItemType> predicate);

    default ExtendedList<ItemType> filter(Matcher matcher) {
        return filter(matcher::matches);
    }

    default ExtendedList<ItemType> filter(String description, Predicate<ItemType> predicate) {
        return filter(predicate);
    }

    @WaitUntilMethod
    ExtendedList<ItemType> waitUntil(String message, Matcher matcher);

    @WaitUntilMethod
    ExtendedList<ItemType> waitUntil(String message, Matcher matcher, @Timeout long timeout);

    default ExtendedList<ItemType> waitUntil(Matcher matcher) {
        return waitUntil("", matcher);
    }

    default ExtendedList<ItemType> waitUntil(String message, Predicate<ExtendedList<ItemType>> predicate) {
        return waitUntil(message, new PredicateMatcher<>(predicate));
    }

    default ExtendedList<ItemType> waitUntil(Predicate<ExtendedList<ItemType>> predicate) {
        return waitUntil("", predicate);
    }

    @ShouldMethod
    ExtendedList<ItemType> should(String message, Matcher matcher);

    default ExtendedList<ItemType> should(Matcher matcher) {
        return should("", matcher);
    }

    @ToStringMethod
    String toString();

}
