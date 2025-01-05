package com.github.maeda6uiui.mecheditor;

import com.github.maeda6uiui.mechtatel.core.Mechtatel;
import com.github.maeda6uiui.mechtatel.core.MttSettings;
import com.github.maeda6uiui.mechtatel.core.MttWindow;
import com.github.maeda6uiui.mechtatel.core.screen.MttScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    private MttScreen mainScreen;

    @Override
    public void onCreate(MttWindow window) {
        mainScreen = window.createScreen(
                new MttScreen.MttScreenCreateInfo()
                        .setPostProcessingNaborNames(List.of("pp.parallel_light"))
        );
    }

    @Override
    public void onUpdate(MttWindow window) {
        mainScreen.draw();
        window.present(mainScreen);
    }
}
