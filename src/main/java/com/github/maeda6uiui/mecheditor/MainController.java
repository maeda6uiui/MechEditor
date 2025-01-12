package com.github.maeda6uiui.mecheditor;

import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.flag.ImGuiCol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Main controller
 *
 * @author maeda6uiui
 */
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private Runnable cbQuit;

    private Main3DViewModel viewModel3D;

    public MainController(
            MttWindow window,
            MttScreen imguiScreen,
            Runnable cbQuit) {
        this.cbQuit = cbQuit;

        viewModel3D = new Main3DViewModel(window, imguiScreen);
    }

    public void declare() {
        this.setStyle();
        this.declareOpenFileDialog();
        this.declareMainMenuBar();
        this.declareXZView();
        this.declareXYView();
        this.declareYZView();
    }

    public void update(MttWindow window) {
        viewModel3D
                .getSelectedFilepath()
                .get()
                .ifPresent(v -> {
                    try {
                        viewModel3D.loadModel(Paths.get(v));
                    } catch (IOException e) {
                        logger.error("Failed to load model", e);
                    }
                });
        viewModel3D.updateCamera(window);
        viewModel3D.draw();
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

    private void declare3DView() {
        if (ImGui.begin("3D View")) {

            ImGui.end();
        }
    }

    private void declareXZView() {
        if (ImGui.begin("X-Z View")) {

            ImGui.end();
        }
    }

    private void declareXYView() {
        if (ImGui.begin("X-Y View")) {

            ImGui.end();
        }
    }

    private void declareYZView() {
        if (ImGui.begin("Y-Z View")) {

            ImGui.end();
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
