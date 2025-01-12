package com.github.maeda6uiui.mecheditor;

/**
 * View model for the main view
 *
 * @author maeda6uiui
 */
public class MainViewModel {
    private MEProperty<String> selectedFilepath;

    public MainViewModel() {
        selectedFilepath = new MEProperty<>("");
    }

    public MEProperty<String> getSelectedFilepath() {
        return selectedFilepath;
    }
}
