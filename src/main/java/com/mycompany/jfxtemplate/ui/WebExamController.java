/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.core.Jackson;
import com.mycompany.jfxtemplate.core.MyDialog;
import com.mycompany.jfxtemplate.core.SimpleDialogController;
import com.mycompany.jfxtemplate.core.WebViewController;
import com.mycompany.jfxtemplate.core.WorkDir;
import java.io.FileNotFoundException;
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
 * WebViewを使ったサンプル.
 *
 * @author hondou
 */
public class WebExamController extends SimpleDialogController {

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
     * この画面の View. この画面を new FXMLDialog() で、作ったときに設定される
     */
    @MyDialog
    private FXMLDialog myStage;

    /**
     * 作業ディレクトリ.
     */
    @Autowired
    private WorkDir work;

    /**
     * WebViewのコントローラクラス. Javascript からよばれたときに、JavaFX8 は reflection で、
     * メソッドを探す。無名クラスにすると、JavaFX8 がうまく呼び出し先 メソッドを見つけられないようなので inner class にする。
     */
    public final class MyWebController extends WebViewController {

        /**
         * WebView で alert() が呼ばれたときの処理.
         *
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
         * WebView で、エラーが起きた時の処理.
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
         * HTMLが正常に良こまれた後に呼ばれる処理.
         */
        @Override
        public void onWebViewReady() {
            // WebViewの初期化完了で、ボタンとスライダーを有効化する
            button.setDisable(false);
            slider.setDisable(false);
        }

        /**
         * JavaScript側から呼ばれるメソッド.
         *
         * @param val スライダーの値
         */
        public void slideTo(final double val) {
            slider.setValue(val);
        }
        
        /**
         * JavaScript側から呼ばれるメソッド.
         * JSON文字列をBeanに変換する
         * @param jsonString 
         */
        public void getJson(final String jsonString) throws IOException {
            MountainBean bean = Jackson.objectMapper.readValue(jsonString, MountainBean.class);
            
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(bean.toString());

            alert.showAndWait();
        }
    }

    /**
     * WebViewのコントローラクラス.
     */
    private MyWebController myWebController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myWebController = new MyWebController();
        // Java FX の WebView コンポーネントを割り付け
        myWebController.bind(webview);
        // HTML のロード
        final Object[][] params = new Object[][]{
            {"root", work.getURL() + "html"}
        };
        myWebController.loadContentAsync("/velocity/index.vm", params);
    }

    /**
     * 送信ボタンが押されたときの処理.
     * Bean を JSON文字列化して Javascript を呼び出す。
     * @param event Event
     */
    @FXML
    public void handleSendButtonAction(final ActionEvent event) throws JsonProcessingException {
        MountainBean fujisan = new MountainBean("富士山", 3776);
        myWebController.callJs("getJson", Jackson.objectMapper.writeValueAsString(fujisan));
    }

    /**
     * スライダーが動いたときの処理.
     *
     * @param event Event
     */
    @FXML
    public void handleSliderDragDetectedAction(final MouseEvent event) {
        myWebController.callJs("updateSlider", slider.getValue());
    }
}
