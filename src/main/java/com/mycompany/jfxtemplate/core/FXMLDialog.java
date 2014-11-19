/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extended Java FX Stage.
 * The Java FX bind fxml to new dialog-controller object ordinally.
 * But we want to transfer the management of dialog-controller to Spring Contaier.
 * Stephen Chin presented how to bind dialog-controller object managed by
 * Spring Container to fxml at JavaFX in Spring Day 2, 2012.
 * http://steveonjava.com/javafx-in-spring-day-2/
 * This was made by reference in it.
 * @author atsushi
 */
public class FXMLDialog extends Stage {
    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(FXMLDialog.class);

    /**
     * Constructor.
     * Modal Window
     *
     * @param controller Controller
     * @param owner Parent Dialog
     */
    public FXMLDialog(final DialogController controller, final Window owner) {
        this(controller, owner, StageStyle.DECORATED, Modality.WINDOW_MODAL);
    }

    /**
     * Constructor.
     *
     * @param controller Controller
     * @param owner Parent Dialog
     * @param modal Modality
     */
    public FXMLDialog(final DialogController controller, final Window owner,
            final Modality modal) {
        this(controller, owner, StageStyle.DECORATED, modal);
    }

    /**
     * Constructor.
     * Modal Window
     *
     * @param controller Controller
     * @param owner Parent Dialog
     * @param style Window Style
     */
    public FXMLDialog(final DialogController controller, final Window owner,
            final StageStyle style) {
        this(controller, owner, style, Modality.WINDOW_MODAL);
    }

    /**
     * Constructor.
     *
     * @param controller Controller
     * @param owner Parent Dialog
     * @param style Window Style
     * @param modal Modality
     */
    public FXMLDialog(final DialogController controller, final Window owner,
            final StageStyle style, final Modality modal) {
        super(style);

        LOGGER.trace("Create FXMLDialog. fxml={}", controller.fxml());

        initOwner(owner);
        initModality(modal);
        final URL fxml = controller.getClass().getResource(controller.fxml());
        final FXMLLoader loader = new FXMLLoader(fxml);
        try {
            loader.setControllerFactory((final Class<?> aClass) -> controller);
            setScene(new Scene((Parent) loader.load()));
            setTitle(controller.title());
            getIcons().add(controller.getIcon());

            wireThis(controller);
        } catch (IOException e) {
            LOGGER.error("Faild to create FXMLDialog", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Inject this FXMLDialog.
     * Inject this FXMLDialog to the field variable annotated by &amp;Mydialog
     * in the argument controller.
     * @param controller Controller
     */
    private void wireThis(final DialogController controller) {

        for (Field f : controller.getClass().getDeclaredFields()) {
            for (Annotation a : f.getAnnotations()) {
                if (a instanceof MyDialog) {
                    f.setAccessible(true);
                    try {
                        f.set(controller, this);
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        LOGGER.error("Can't inject FXMLDalig to @MyDialog", ex);
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }
}
