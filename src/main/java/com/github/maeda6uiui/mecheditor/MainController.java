package com.github.maeda6uiui.mecheditor;

import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.camera.FreeCamera;
import com.github.maeda6uiui.mechtatel.core.input.keyboard.KeyCode;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Main controller
 *
 * @author maeda6uiui
 */
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private Runnable cbQuit;

    private Map<String, MainViewModel> viewModels;
    private Map<String, Boolean> viewFocusedFlags;

    public MainController(
            MttWindow window,
            MttScreen imguiScreen,
            Runnable cbQuit) {
        this.cbQuit = cbQuit;

        viewModels = new HashMap<>();
        var viewModel3D = new Main3DViewModel(window, imguiScreen);
        viewModels.put("3d", viewModel3D);

        viewFocusedFlags = new HashMap<>();
        viewFocusedFlags.put("3d", false);
    }

    public void declare() {
        this.setStyle();
        this.declareOpenFileDialog();
        this.declareMainMenuBar();
        this.declare3DView();
        this.declareXZView();
        this.declareXYView();
        this.declareYZView();
    }

    private void loadModel(String modelFilepath) {
        for (var viewModel : viewModels.values()) {
            try {
                viewModel.loadModel(Paths.get(modelFilepath));
            } catch (IOException e) {
                logger.error("Failed to load model", e);
                break;
            }
        }
    }

    public void update(MttWindow window) {
        viewModels.forEach((k, v) -> {
            v
                    .getSelectedFilepath()
                    .get()
                    .ifPresent(this::loadModel);

            boolean focused = viewFocusedFlags.get(k);
            if (focused) {
                FreeCamera camera = v.getCamera();
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
            }

            v.draw();
        });
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
                        .ifPresent(e -> {
                            viewModels
                                    .values()
                                    .forEach(v -> v.getSelectedFilepath().set(e.getValue()));
                        });
            }
            ImGuiFileDialog.close();
        }
    }

    private void declare3DView() {
        MainViewModel viewModel3D = viewModels.get("3d");

        if (ImGui.begin("3D View")) {
            viewFocusedFlags.put("3d", ImGui.isWindowFocused());

            ImGui.setWindowPos(50, 50, ImGuiCond.FirstUseEver);
            ImGui.setWindowSize(640, 480, ImGuiCond.FirstUseEver);
            ImGui.image(
                    viewModel3D.getScreenImageAllocationIndex(),
                    ImGui.getContentRegionAvailX(),
                    ImGui.getContentRegionAvailY()
            );
        }
        ImGui.end();
    }

    private void declareXZView() {
        if (ImGui.begin("X-Z View")) {
            viewFocusedFlags.put("xz", ImGui.isWindowFocused());

            ImGui.setWindowPos(100, 100, ImGuiCond.FirstUseEver);
            ImGui.setWindowSize(640, 480, ImGuiCond.FirstUseEver);
        }
        ImGui.end();
    }

    private void declareXYView() {
        if (ImGui.begin("X-Y View")) {
            viewFocusedFlags.put("xy", ImGui.isWindowFocused());

            ImGui.setWindowPos(150, 150, ImGuiCond.FirstUseEver);
            ImGui.setWindowSize(640, 480, ImGuiCond.FirstUseEver);
        }
        ImGui.end();
    }

    private void declareYZView() {
        if (ImGui.begin("Y-Z View")) {
            viewFocusedFlags.put("yz", ImGui.isWindowFocused());

            ImGui.setWindowPos(200, 200, ImGuiCond.FirstUseEver);
            ImGui.setWindowSize(640, 480, ImGuiCond.FirstUseEver);
        }
        ImGui.end();
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
