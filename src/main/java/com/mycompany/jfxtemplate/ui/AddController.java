/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.ui;

import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.core.MyDialog;
import com.mycompany.jfxtemplate.core.AbstractDialogController;
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
public class AddController extends AbstractDialogController {
    
    @Autowired
    private BizLogic bizLogic;
    
    @FXML
    private TextField txtNumber;
    
    /**
     * Window.
     * this will bind when new FXMLDialog()
     */
    @MyDialog
    private FXMLDialog myStage;

    /**
     * will be called when "Calc" button is clicked.
     * @param event Event
     */
    @FXML
    public void handleCalcButtonAction(final ActionEvent event) {
        try {
            // Call business logic
            bizLogic.add(Integer.parseInt(txtNumber.getText()));
            // notify I was changed to parent controller (MainController)
            this.setChanged();
            this.notifyObservers();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }        
        myStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // do nothing
    }
}
