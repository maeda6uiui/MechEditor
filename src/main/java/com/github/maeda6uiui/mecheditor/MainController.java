package com.github.maeda6uiui.mecheditor;

import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.camera.FreeCamera;
import com.github.maeda6uiui.mechtatel.core.input.keyboard.KeyCode;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
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

    private MainViewModel mainViewModel;
    private boolean shouldLoadModel;

    public MainController(
            MttWindow window,
            MttScreen imguiScreen,
            Runnable cbQuit) {
        this.cbQuit = cbQuit;

        mainViewModel = new MainViewModel(window, imguiScreen);
        shouldLoadModel = false;
    }

    public void declare() {
        this.setStyle();
        this.declareOpenFileDialog();
        this.declareMainView();
        this.declareMainMenuBar();
    }

    private void loadModel(String modelFilepath) {
        try {
            mainViewModel.loadModel(Paths.get(modelFilepath));
        } catch (IOException e) {
            logger.error("Failed to load model", e);
        }
    }

    public void update(MttWindow window, MttScreen imguiScreen) {
        if (shouldLoadModel) {
            Map<String, String> selections = ImGuiFileDialog.getSelection();
            selections
                    .entrySet()
                    .stream()
                    .findFirst()
                    .ifPresent(e -> this.loadModel(e.getValue()));
            shouldLoadModel = false;
        }

        FreeCamera camera = mainViewModel.getCamera();
        camera.translate(
                window.getKeyboardPressingCount(KeyCode.W),
                window.getKeyboardPressingCount(KeyCode.S),
                window.getKeyboardPressingCount(KeyCode.A),
                window.getKeyboardPressingCount(KeyCode.D)
        );
        camera.rotate(
                window.getKeyboardPressingCount(KeyCode.UP),
                window.getKeyboardPressingCount(KeyCode.DOWN),
                window.getKeyboardPressingCount(KeyCode.LEFT),
                window.getKeyboardPressingCount(KeyCode.RIGHT)
        );

        mainViewModel.draw();
    }

    private void setStyle() {
        ImGui.getStyle().setColor(ImGuiCol.ModalWindowDimBg, 1.0f, 1.0f, 1.0f, 0.01f);
    }

    private void declareOpenFileDialog() {
        if (ImGuiFileDialog.display("open_file", ImGuiFileDialogFlags.None, 400, 300)) {
            if (ImGuiFileDialog.isOk()) {
                shouldLoadModel = true;
            }
            ImGuiFileDialog.close();
        }
    }

    private void declareMainView() {
        ImGuiIO io = ImGui.getIO();
        ImVec2 windowSize = io.getDisplaySize();

        ImDrawList drawList = ImGui.getBackgroundDrawList();
        drawList.addImage(
                mainViewModel.getScreenImageAllocationIndex(),
                new ImVec2(0, 0),
                windowSize
        );
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
