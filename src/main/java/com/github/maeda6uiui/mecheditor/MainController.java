package com.github.maeda6uiui.mecheditor;

import imgui.ImGui;

/**
 * Main controller
 *
 * @author maeda6uiui
 */
public class MainController {
    private Runnable cbQuit;

    public MainController(Runnable cbQuit) {
        this.cbQuit = cbQuit;
    }

    public void declare() {
        this.declareMainMenuBar();
    }

    private void declareMainMenuBar() {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("New", "Ctrl+N")) {

                }
                if (ImGui.menuItem("Open", "Ctrl+O")) {

                }
                ImGui.separator();
                if (ImGui.menuItem("Save", "Ctrl+S")) {

                }
                if (ImGui.menuItem("Save As", "Ctrl+Shift+S")) {

                }
                ImGui.separator();
                if (ImGui.menuItem("Quit", "Ctrl+Q")) {
                    cbQuit.run();
                }
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Help")) {
                if (ImGui.menuItem("About")) {

                }
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }
    }
}
