/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.ui;

import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.core.MyDialog;
import com.mycompany.jfxtemplate.core.SimpleDialogController;
import com.mycompany.jfxtemplate.d3.D3ExamController;
import com.mycompany.jfxtemplate.mdl.Entity;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main Controller Class.
 */
public class MainController extends SimpleDialogController implements Observer {

    @Autowired
    private AddController addController;

    @Autowired
    private SubController subController;

    @Autowired
    private D3ExamController d3ExamController;

    @Autowired
    private WebExamController webExamController;

    @Autowired
    private Entity entity;

    @FXML
    private Label txtSum;

    /**
     * この画面の View. この画面を new FXMLDialog() で、作ったときに設定される
     */
    @MyDialog
    private FXMLDialog myStage;

    /**
     * 足し算リンクが押されたときの処理.
     *
     * @param event Event
     */
    @FXML
    public void handleAddAction(final ActionEvent event) {
        FXMLDialog addDialog = addController.getMyStage();
        if (null == addDialog) {
            // Add 画面を作る
            addDialog = new FXMLDialog(addController, myStage, Modality.APPLICATION_MODAL);
        }
        addDialog.show();
    }

    /**
     * 引き算リンクが押されたときの処理.
     * 画面の表示内容を保持したいときには、Controller 内に保持されている画面
     * オブジェクト(stage) を使いまわす
     * @param event Event
     */
    @FXML
    public void handleSubAction(final ActionEvent event) {
        FXMLDialog subDialog = subController.getMyStage();
        if (null == subDialog) {
            // Add 画面を作る
            subDialog = new FXMLDialog(subController, myStage, Modality.APPLICATION_MODAL);
        }
        subDialog.show();
    }

    /**
     * 3Dが押されたときの処理.
     *
     * @param event Event
     */
    @FXML
    public void handle3DAction(final ActionEvent event) {
        // 3D Dialog 画面を作る
        final FXMLDialog d3Dialog = new FXMLDialog(d3ExamController, myStage);
        d3Dialog.show();
    }

    /**
     * Webが押されたときの処理.
     *
     * @param event Event
     */
    @FXML
    public void handleWebViewAction(final ActionEvent event) {
        // Web View 画面を作る
        final FXMLDialog webExamDialog = new FXMLDialog(webExamController, myStage);
        webExamDialog.show();
    }

    /**
     * 初期化.
     *
     * @param url URL
     * @param bundle Bundle
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        // Add 画面の変更を監視する
        addController.addObserver(this);
        // Sub 画面の変更を監視する
        subController.addObserver(this);
    }

    /**
     * 小画面の変更イベントを処理する.
     *
     * @param o 小画面コントローラ
     * @param arg メッセージ
     */
    @Override
    public void update(Observable o, Object arg) {
        txtSum.setText(String.format("Value=%d", entity.getVal()));
    }
}
