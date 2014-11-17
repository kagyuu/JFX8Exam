/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
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
import javax.xml.ws.soap.Addressing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Sub Controller class
 *
 * @author atsushi
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SubController extends SimpleDialogController {
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
            bizLogic.sub(Integer.parseInt(txtNumber.getText()));
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
