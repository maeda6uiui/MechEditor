package com.github.maeda6uiui.mecheditor;

import com.github.maeda6uiui.mechtatel.core.Mechtatel;
import com.github.maeda6uiui.mechtatel.core.MttSettings;
import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import com.github.maeda6uiui.mechtatel.core.screen.component.MttImGui;
import imgui.ImFont;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of MechEditor
 *
 * @author maeda6uiui
 */
public class MechEditorMain extends Mechtatel {
    private static final Logger logger = LoggerFactory.getLogger(MechEditorMain.class);

    public MechEditorMain(MttSettings settings) {
        super(settings);
        this.run();
    }

    public static void main(String[] args) {
        MttSettings
                .load("./MechEditor/settings.json")
                .ifPresentOrElse(
                        MechEditorMain::new,
                        () -> logger.error("Failed to load settings")
                );
    }

    private MttScreen imGuiScreen;
    private MttImGui imGui;
    private MainController mainController;

    @Override
    public void onCreate(MttWindow window) {
        imGuiScreen = window.createScreen(new MttScreen.MttScreenCreateInfo());

        //Load font before creating ImGui instance
        ImGui.setCurrentContext(window.getImGuiContext());
        ImGuiIO io = ImGui.getIO();
        ImFontAtlas fonts = io.getFonts();
        ImFont font = fonts.addFontFromFileTTF("./MechEditor/Font/Roboto/Roboto-Regular.ttf", 18.0f);
        io.setFontDefault(font);
        fonts.build();

        imGui = imGuiScreen.createImGui();
        mainController = new MainController(
                window,
                imGuiScreen,
                this::closeAllWindows
        );
    }

    @Override
    public void onUpdate(MttWindow window) {
        imGui.declare(mainController::declare);
        mainController.update(window);

        imGuiScreen.draw();
        window.present(imGuiScreen);
    }
}
