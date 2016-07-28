package io.qameta.htmlelements;

import io.qameta.htmlelements.example.element.SearchArrow;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public class ReflectionTest {

    @Test
    public void testOutput() {
        System.out.println(ClassUtils.getAllInterfaces(SearchArrow.class));
    }
}
