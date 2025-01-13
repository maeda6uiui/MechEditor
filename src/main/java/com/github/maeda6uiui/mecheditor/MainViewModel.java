package com.github.maeda6uiui.mecheditor;

import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.camera.FreeCamera;
import com.github.maeda6uiui.mechtatel.core.postprocessing.PostProcessingProperties;
import com.github.maeda6uiui.mechtatel.core.postprocessing.light.ParallelLight;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import com.github.maeda6uiui.mechtatel.core.screen.ScreenImageType;
import com.github.maeda6uiui.mechtatel.core.screen.component.MttModel;
import com.github.maeda6uiui.mechtatel.core.screen.texture.MttTexture;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * View model for the main view
 *
 * @author maeda6uiui
 */
public class MainViewModel {
    private MttScreen screen;
    private MttTexture image;
    private FreeCamera camera;

    private MttModel model;

    private MEProperty<String> selectedFilepath;

    public MainViewModel(MttWindow window, MttScreen imguiScreen) {
        screen = window.createScreen(
                new MttScreen.MttScreenCreateInfo()
                        .setPostProcessingNaborNames(List.of("pp.parallel_light"))
                        .setShouldChangeExtentOnRecreate(false)
        );
        image = screen.texturize(ScreenImageType.COLOR, imguiScreen);

        PostProcessingProperties ppProps = screen.getPostProcessingProperties();
        ParallelLight light = ppProps.createParallelLight();

        camera = new FreeCamera(screen.getCamera());

        selectedFilepath = new MEProperty<>("");
    }

    public void loadModel(Path modelFile) throws IOException {
        if (model != null) {
            model.cleanup();
        }

        model = screen.createModel(modelFile);
    }

    public void draw() {
        screen.draw();
    }

    protected MttScreen getScreen() {
        return screen;
    }

    public int getScreenImageAllocationIndex() {
        return image.getVulkanTexture().getAllocationIndex();
    }

    public FreeCamera getCamera() {
        return camera;
    }

    public MEProperty<String> getSelectedFilepath() {
        return selectedFilepath;
    }
}
