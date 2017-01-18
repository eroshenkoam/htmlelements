package io.qameta.htmlelements.element;

import io.qameta.htmlelements.extension.elements.SetFileInputMethod;

/**
 * @author ehborisov
 */
public interface FileInput extends ExtendedWebElement {

    @SetFileInputMethod
    void setFileToInput(String path);
}
