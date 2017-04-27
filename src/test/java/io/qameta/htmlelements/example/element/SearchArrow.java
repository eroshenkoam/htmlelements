package io.qameta.htmlelements.example.element;

import io.qameta.htmlelements.annotation.Description;
import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.annotation.Name;
import io.qameta.htmlelements.annotation.Param;
import io.qameta.htmlelements.element.*;
import io.qameta.htmlelements.example.TestData;
import io.qameta.htmlelements.extension.DescriptionProvider;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface SearchArrow extends WithSuggest<SearchArrow>, ExtendedWebElement<SearchArrow> {

    @Description("Форма {{ className }}")
    @FindBy(TestData.SEARCH_FORM_TEMPLATE)
    SearchForm form(@Param("className") String className);

}
