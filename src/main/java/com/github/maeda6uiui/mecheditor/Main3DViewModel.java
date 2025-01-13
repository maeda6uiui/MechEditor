package com.github.maeda6uiui.mecheditor;

import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.camera.FreeCamera;
import com.github.maeda6uiui.mechtatel.core.input.keyboard.KeyCode;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import com.github.maeda6uiui.mechtatel.core.screen.ScreenImageType;
import com.github.maeda6uiui.mechtatel.core.screen.component.MttModel;
import com.github.maeda6uiui.mechtatel.core.screen.texture.MttTexture;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * View model for the main 3D view
 *
 * @author maeda6uiui
 */
public class Main3DViewModel {
    private MttScreen screen;
    private MttTexture image;
    private MttModel model;
    private FreeCamera camera;

    private MEProperty<String> selectedFilepath;

    public Main3DViewModel(MttWindow window, MttScreen imguiScreen) {
        screen = window.createScreen(
                new MttScreen.MttScreenCreateInfo()
                        .setPostProcessingNaborNames(List.of("pp.parallel_light"))
                        .setShouldChangeExtentOnRecreate(false)
        );
        camera = new FreeCamera(screen.getCamera());
        image = screen.texturize(ScreenImageType.COLOR, imguiScreen);

        selectedFilepath = new MEProperty<>("");
    }

    public void loadModel(Path modelFile) throws IOException {
        if (model != null) {
            model.cleanup();
        }

        model = screen.createModel(modelFile);
    }

    public void updateCamera(MttWindow window) {
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

    public void draw() {
        screen.draw();
    }

    public int getScreenImageAllocationIndex() {
        return image.getVulkanTexture().getAllocationIndex();
    }

    public MEProperty<String> getSelectedFilepath() {
        return selectedFilepath;
    }
}
