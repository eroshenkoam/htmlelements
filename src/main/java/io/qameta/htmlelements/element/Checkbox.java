package io.qameta.htmlelements.element;

/**
 * @author ehborisov
 */
public interface Checkbox extends ExtendedWebElement {

    default void setChecked(boolean state) {
        if(isSelected() != state){
            click();
        }
    }
}
