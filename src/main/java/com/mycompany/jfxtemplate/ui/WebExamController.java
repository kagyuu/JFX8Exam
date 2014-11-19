/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.core.Jackson;
import com.mycompany.jfxtemplate.core.MyDialog;
import com.mycompany.jfxtemplate.core.AbstractDialogController;
import com.mycompany.jfxtemplate.core.WebViewController;
import com.mycompany.jfxtemplate.core.WorkDir;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Web Exam Controller.
 * This is an example for webview.
 * @author atsushi
 */
public class WebExamController extends AbstractDialogController {

    /**
     * WebView.
     */
    @FXML
    private WebView webview;

    /**
     * Button.
     */
    @FXML
    private Button button;

    /**
     * Slider.
     */
    @FXML
    private Slider slider;

    /**
     * Window.
     */
    @MyDialog
    private FXMLDialog myStage;

    /**
     * Workdir.
     */
    @Autowired
    private WorkDir work;

    /**
     * WebView Controller.
     * You must create inner class to handler webview.
     * Java FX can't invoke a method in the anonumous class.
     * anonumous class is like this :
     * WebController myController = new WebController() { ... };
     */
    public final class MyWebController extends WebViewController {

        /**
         * handler javasctipt:alert.
         * @param event Event
         */
        @Override
        public void handleAlert(final WebEvent<String> event) {
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(event.getData());

            alert.showAndWait();
        }

        /**
         * handler javascript error.
         * @param th Throwable
         */
        @Override
        public void handleLoadError(final Throwable th) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Look, an Exception Dialog");
            alert.setContentText(th.getMessage());

            // Create expandable Exception.
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            th.printStackTrace(pw);
            final String exceptionText = sw.toString();

            final Label label = new Label("The exception stacktrace was:");

            final TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            final GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }

        /**
         * handler javascript ready.
         * enable button and slider that will call javasctipt:function().
         */
        @Override
        public void onWebViewReady() {
            button.setDisable(false);
            slider.setDisable(false);
        }

        /**
         * An examle of calling from javascript with simple args.
         *
         * @param val slider value
         */
        public void slideTo(final double val) {
            slider.setValue(val);
        }

        /**
          * An examle of calling from javascript with complex args.
          * Complex args shoud be JSON String. Javascript has own variable types
          * and Java has so. It's difficult to manage complex args as hierarchic
          * object.
          * @param jsonString args
          * @throws IOException failt to parse jsonString
          */
        public void getJson(final String jsonString) throws IOException {
            // Parse json string to bean
            final MountainBean bean
              = Jackson.objectMapper.readValue(jsonString, MountainBean.class);

            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(bean.toString());

            alert.showAndWait();
        }
    }

    /**
     * WebView Controller.
     */
    private MyWebController myWebController;

    /**
     * initialize this controller.
     * @param url URL
     * @param bundle Bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myWebController = new MyWebController();
        // bind webview to webview-controller
        myWebController.bind(webview);
        // load HTML
        final Object[][] params = new Object[][]{
            {"root", work.getURL() + "html"}
        };
        myWebController.loadContentAsync("/velocity/index.vm", params);
    }

    /**
     * will be called when "Send" buttond was clickded.
     * Send Bean as JSON String
     * @param event Event
     * @throws JsonProcessingException failed to serialize bean
     */
    @FXML
    public void handleSendButtonAction(final ActionEvent event)
            throws JsonProcessingException {

        // 富士山 is Mt.Fuji in English. Send multi-byte character.
        final MountainBean fujisan = new MountainBean("富士山", 3776);
        // Call javascript:function getJson(json)
        myWebController.callJs(
            "getJson",
            Jackson.objectMapper.writeValueAsString(fujisan));
    }

    /**
     * will be called when "Slider" was moved.
     * Did you know event type? You can try set arg type Event and display
     * type by System.out.println(event.getClass().getCanonicalName());.
     * @param event Event
     */
    @FXML
    public void handleSliderDragDetectedAction(final MouseEvent event) {
        myWebController.callJs("updateSlider", slider.getValue());
    }
}
