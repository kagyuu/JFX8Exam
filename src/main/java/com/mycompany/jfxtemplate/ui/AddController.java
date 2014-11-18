/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.ui;

import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.core.MyDialog;
import com.mycompany.jfxtemplate.core.SimpleDialogController;
import com.mycompany.jfxtemplate.tx.BizLogic;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Add Controller class.
 *
 * @author atsushi
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class AddController extends SimpleDialogController {
    
    @Autowired
    private BizLogic bizLogic;
    
    @FXML
    private TextField txtNumber;
    
    /**
     * この画面の View.
     * この画面を new FXMLDialog() で、作ったときに設定される
     */
    @MyDialog
    private FXMLDialog myStage;

    /**
     * 計算ボタンが押されたときの処理.
     * @param event Event
     */
    @FXML
    public void handleCalcButtonAction(final ActionEvent event) {
        try {
            // 業務処理
            bizLogic.add(Integer.parseInt(txtNumber.getText()));
            // このオブジェクトに変更フラグを設定し、監視しているオブジェクトに変更通知
            this.setChanged();
            this.notifyObservers();
        } catch(NumberFormatException ex) {
            ex.printStackTrace();
        }        
        myStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
