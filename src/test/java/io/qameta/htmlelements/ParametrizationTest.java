package io.qameta.htmlelements;

import io.qameta.htmlelements.example.element.SearchArrow;
import io.qameta.htmlelements.util.ReflectionUtils;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * @author nogert <nogert@yandex-team.ru>
 */
public class ParametrizationTest {

    private static final String TEST_FORM_NAME = "test form name";
    private static final String[] SEARCH_FORM_PARAMETERS = {TEST_FORM_NAME};

    @Test
    public void elementWithParametrizedDescriptionShouldHasCorrectName() throws NoSuchMethodException {
        Method searchForm = getSearchFormMethod();
        String description = ReflectionUtils.getDescription(searchForm, new String[]{TEST_FORM_NAME});
        assertEquals(String.format("Форма %s", TEST_FORM_NAME), description);

    }

    @Test
    public void elementWithParametrizedFindByShouldHasCorrectXpath() throws NoSuchMethodException {
        Method searchForm = getSearchFormMethod();
        String selector = ReflectionUtils.getSelector(searchForm, SEARCH_FORM_PARAMETERS);

        assertEquals(String.format("//div[@class='%s']", TEST_FORM_NAME), selector);
    }

    private Method getSearchFormMethod() throws NoSuchMethodException {
        return ReflectionUtils.getMethods(SearchArrow.class)
                    .stream()
                    .filter(m -> m.getName().equals("form"))
                    .findFirst().orElseThrow(NoSuchMethodException::new);
    }
}
