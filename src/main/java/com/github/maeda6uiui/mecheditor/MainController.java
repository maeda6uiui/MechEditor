package com.github.maeda6uiui.mecheditor;

import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.flag.ImGuiCol;

import java.util.Map;

/**
 * Main controller
 *
 * @author maeda6uiui
 */
public class MainController {
    private Runnable cbQuit;

    private Main3DViewModel viewModel3D;

    public MainController(Runnable cbQuit) {
        this.cbQuit = cbQuit;

        viewModel3D = new Main3DViewModel();
    }

    public void declare() {
        this.setStyle();
        this.declareOpenFileDialog();
        this.declareMainMenuBar();
    }

    private void setStyle() {
        ImGui.getStyle().setColor(ImGuiCol.ModalWindowDimBg, 1.0f, 1.0f, 1.0f, 0.01f);
    }

    private void declareOpenFileDialog() {
        if (ImGuiFileDialog.display("open_file", ImGuiFileDialogFlags.None, 400, 300)) {
            if (ImGuiFileDialog.isOk()) {
                Map<String, String> selections = ImGuiFileDialog.getSelection();
                selections
                        .entrySet()
                        .stream()
                        .findFirst()
                        .ifPresent(e -> viewModel3D.getSelectedFilepath().set(e.getValue()));
            }
            ImGuiFileDialog.close();
        }
    }

    private void declareMainMenuBar() {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("New", "Ctrl+N")) {

                }
                if (ImGui.menuItem("Open", "Ctrl+O")) {
                    ImGuiFileDialog.openModal("open_file", "Open File", ".bd1", "");
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
