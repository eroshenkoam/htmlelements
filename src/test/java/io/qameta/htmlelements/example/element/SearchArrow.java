package io.qameta.htmlelements.example.element;

import io.qameta.htmlelements.annotation.FindBy;
import io.qameta.htmlelements.annotation.Param;
import io.qameta.htmlelements.example.TestData;

/**
 * @author Artem Eroshenko <erosenkoam@me.com>
 */
public interface SearchArrow extends WithSuggest {


    @FindBy(TestData.SEARCH_FORM_TEMPLATE)
    SearchForm form(@Param("className") String className);

}
