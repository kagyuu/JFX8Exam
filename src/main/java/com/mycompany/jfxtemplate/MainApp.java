/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate;

import com.mycompany.jfxtemplate.core.AppConfiguration;
import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.ui.UiConfiguration;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * boot class.
 * @author atsushi
 */
public final class MainApp extends Application {

    /**
     * Initialize process.
     * This procedure run on the JavaFX application thread.
     * @param stage Stage
     */
    @Override
    public void start(final Stage stage) {
        // wake up Spring Container on the JavaFX application thread.
        final ApplicationContext context
                = new AnnotationConfigApplicationContext(AppConfiguration.class);

        // initialize the workdir.
        final AppConfiguration app = context.getBean(AppConfiguration.class);
        app.workdir().init();

        // get UIConfiguration from the Spring container.
        final UiConfiguration uiConfig = context.getBean(UiConfiguration.class);

        // get Main Menu from the Spring container
        final FXMLDialog mainStage = new FXMLDialog(uiConfig.mainController(), stage);
        mainStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
