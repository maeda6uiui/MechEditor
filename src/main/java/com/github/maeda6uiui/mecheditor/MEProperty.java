package com.github.maeda6uiui.mecheditor;

import java.util.Optional;

/**
 * Property
 *
 * @param <T> Type
 * @author maeda6uiui
 */
public class MEProperty<T> {
    private T property;
    private boolean contentChanged;

    public MEProperty(T property) {
        this.property = property;
        contentChanged = false;
    }

    /**
     * Sets the property.
     *
     * @param property Property
     */
    public void set(T property) {
        this.property = property;
        contentChanged = true;
    }

    /**
     * Returns the property if the underlying property has been changed.
     *
     * @return Property
     */
    public Optional<T> get() {
        if (contentChanged) {
            contentChanged = false;
            return Optional.of(property);
        } else {
            return Optional.empty();
        }
    }
}
