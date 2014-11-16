/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
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
    
    private FXMLDialog addDialog;

    @Autowired
    private SubController subController;
    
    private FXMLDialog subDialog;
    
    @Autowired
    private D3ExamController d3ExamController;
    
    @Autowired
    private Entity entity;

    @FXML
    private Label txtSum;
    
    /**
     * この画面の View.
     * この画面を new FXMLDialog() で、作ったときに設定される
     */
    @MyDialog
    private FXMLDialog myStage;

    /**
     * 足し算リンクが押されたときの処理.
     * @param event Event
     */
    @FXML
    public void handleAddAction(final ActionEvent event) {
        addDialog.show();
    }

    /**
     * 引き算リンクが押されたときの処理.
     * @param event Event
     */
    @FXML
    public void handleSubAction(final ActionEvent event) {
        subDialog.show();
    }
    
    /**
     * 3Dが押されたときの処理.
     * @param event Event
     */
    @FXML
    public void handle3DAction(final ActionEvent event) {
        FXMLDialog d3Dialog = new FXMLDialog(d3ExamController, myStage, Modality.APPLICATION_MODAL);
        d3Dialog.show();
    }
    
    /**
     * 初期化.
     *
     * @param url
     *            URL
     * @param bundle
     *            Bundle
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        // Add 画面を作る
        addDialog = new FXMLDialog(addController, myStage, Modality.APPLICATION_MODAL);
        // Sub 画面を作る
        subDialog = new FXMLDialog(subController, myStage, Modality.APPLICATION_MODAL);

        // Add 画面の変更を監視する
        addController.addObserver(this);
        // Sub 画面の変更を監視する
        subController.addObserver(this);
    }

    /**
     * 小画面の変更イベントを処理する.
     * @param o 小画面コントローラ
     * @param arg メッセージ
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("UPDATE");
        txtSum.setText(String.format("Value=%d", entity.getVal()));
    }
}
