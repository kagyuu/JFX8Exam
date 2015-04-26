/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate;

import com.mycompany.jfxtemplate.core.AppConfiguration;
import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.ui.UiConfiguration;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * boot class.
 * @author atsushi
 */
public final class MainApp extends Application {
    
    private ApplicationContext springContext;

    /**
     * Initialize process.
     * This procedure run on the JavaFX application thread.
     * @param stage Stage
     */
    @Override
    public void start(final Stage stage) {        
        // get UIConfiguration from the Spring container.
        final UiConfiguration uiConfig = springContext.getBean(UiConfiguration.class);
        // get Main Menu from the Spring container
        final FXMLDialog mainStage = new FXMLDialog(uiConfig.mainController(), stage);
        mainStage.show();
    }
    
    
    @Override
    public void init() {
        // wake up Spring Container on the JavaFX application thread.
        notifyPreloader(new ProgressNotification(0.0f));
        notifyPreloader(new MyPreloaderNotification("wake up String Container ..."));
        springContext = new AnnotationConfigApplicationContext(AppConfiguration.class);
        notifyPreloader(new MyPreloaderNotification("wake up String Container ... done"));

        // initialize the workdir.
        notifyPreloader(new ProgressNotification(0.5f));
        notifyPreloader(new MyPreloaderNotification("initialize the workdir ..."));
        final AppConfiguration app = springContext.getBean(AppConfiguration.class);
        app.workdir().init();
        notifyPreloader(new MyPreloaderNotification("initialize the workdir ... done"));

        // finish !
        notifyPreloader(new MyPreloaderNotification("application was initialized"));
        notifyPreloader(new ProgressNotification(1.0f));
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
