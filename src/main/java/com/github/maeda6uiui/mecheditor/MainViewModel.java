package com.github.maeda6uiui.mecheditor;

import com.github.dabasan.jxm.bd1.BD1Block;
import com.github.dabasan.jxm.bd1.BD1Manipulator;
import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.camera.FreeCamera;
import com.github.maeda6uiui.mechtatel.core.postprocessing.PostProcessingProperties;
import com.github.maeda6uiui.mechtatel.core.postprocessing.light.ParallelLight;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import com.github.maeda6uiui.mechtatel.core.screen.ScreenImageType;
import com.github.maeda6uiui.mechtatel.core.screen.component.MttLineSet;
import com.github.maeda6uiui.mechtatel.core.screen.texture.MttTexture;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * View model for the main 3D view
 *
 * @author maeda6uiui
 */
public class MainViewModel {
    private MttScreen screen;
    private MttTexture image;
    private FreeCamera camera;

    private List<BD1Block> blocks;
    private MttLineSet blockEdges;

    public MainViewModel(MttWindow window, MttScreen imguiScreen) {
        screen = window.createScreen(
                new MttScreen.MttScreenCreateInfo()
                        .setPostProcessingNaborNames(List.of("pp.parallel_light"))
                        .setShouldChangeExtentOnRecreate(false)
        );
        image = screen.texturize(ScreenImageType.COLOR, imguiScreen);

        PostProcessingProperties ppProps = screen.getPostProcessingProperties();
        ParallelLight light = ppProps.createParallelLight();

        MttLineSet axis = screen.createLineSet();
        axis.addAxes(5.0f);
        axis.createBuffer();

        camera = new FreeCamera(screen.getCamera());
    }

    public void cleanup() {
        image.cleanup();
        screen.cleanup();
    }

    public void loadModel(Path modelFile) throws IOException {
        //Create line set for block edges
        final float BD1_MODEL_SCALE = 1.7f / 20.0f;

        var manipulator = new BD1Manipulator(modelFile.toFile());
        manipulator.rescale(BD1_MODEL_SCALE, BD1_MODEL_SCALE, BD1_MODEL_SCALE).applyTransformation();
        blocks = manipulator.getBlocks();

        blockEdges = screen.createLineSet();
        blocks.forEach(block -> {
            //Up
            for (int i = 0; i < 4; i++) {
                blockEdges.add(
                        new Vector3f(block.vertexPositions[i]),
                        new Vector4f(1.0f),
                        new Vector3f(block.vertexPositions[(i + 1) % 4]),
                        new Vector4f(1.0f)
                );
            }
            //Down
            for (int i = 0; i < 4; i++) {
                blockEdges.add(
                        new Vector3f(block.vertexPositions[4 + i]),
                        new Vector4f(1.0f),
                        new Vector3f(block.vertexPositions[4 + (i + 1) % 4]),
                        new Vector4f(1.0f)
                );
            }
            //Vertical
            for (int i = 0; i < 4; i++) {
                blockEdges.add(
                        new Vector3f(block.vertexPositions[i]),
                        new Vector4f(1.0f),
                        new Vector3f(block.vertexPositions[i + 4]),
                        new Vector4f(1.0f)
                );
            }
        });
        blockEdges.createBuffer();
    }

    public void draw() {
        screen.draw();
    }

    public int getScreenImageAllocationIndex() {
        return image.getVulkanTexture().getAllocationIndex();
    }

    public FreeCamera getCamera() {
        return camera;
    }
}
