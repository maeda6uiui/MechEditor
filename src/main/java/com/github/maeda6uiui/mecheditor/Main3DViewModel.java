package com.github.maeda6uiui.mecheditor;

/**
 * View model for the main 3D view
 *
 * @author maeda6uiui
 */
public class Main3DViewModel {
    private MEProperty<String> selectedFilepath;

    public Main3DViewModel() {
        selectedFilepath = new MEProperty<>("");
    }

    public MEProperty<String> getSelectedFilepath() {
        return selectedFilepath;
    }
}
