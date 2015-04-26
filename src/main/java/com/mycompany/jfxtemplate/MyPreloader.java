/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jfxtemplate;

import java.util.concurrent.Executors;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author atsushi
 */
public class MyPreloader extends Preloader {

    /**
     * logger.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(MyPreloader.class);

    private long start;
    private ProgressBar bar;
    private Label lbl;
    private Stage stage;

    private Scene createPreloaderScene() {

        // Image
        Image image = new Image(Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("splash.jpg"));
        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.drawImage(image, 0, 0);

        // Progress Bar
        bar = new ProgressBar();
        bar.setMinWidth(image.getWidth());

        // Message
        lbl = new Label();
        lbl.setMinWidth(image.getWidth());
        lbl.setAlignment(Pos.BOTTOM_RIGHT);

        // Scene
        VBox p = new VBox();
        p.getChildren().addAll(canvas, bar, lbl);
        Scene scene = new Scene(p);

        return scene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("Preloader Start");
        this.start = System.currentTimeMillis();
        this.stage = stage;
        stage.setScene(createPreloaderScene());
        stage.show();
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        LOGGER.info(evt.getType().toString());
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            LOGGER.info("Preloader End");
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    Thread.sleep(start + 3000L - System.currentTimeMillis());
                } catch (IllegalArgumentException | InterruptedException ex) {
                    // ignore ( include sleep is negative )
                } finally {
                    javafx.application.Platform.runLater(() -> {
                        stage.hide();
                    });
                }

            });
        }
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof MyPreloaderNotification) {
            lbl.setText(((MyPreloaderNotification) info).getMessage());
        }
    }
}
