/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.ui;

import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.core.MyDialog;
import com.mycompany.jfxtemplate.core.AbstractDialogController;
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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main Controller Class.
 */
public class MainController extends AbstractDialogController implements Observer {

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
     * Window.
     * this will bind when new FXMLDialog()
     */
    @MyDialog
    private FXMLDialog myStage;

    /**
     * will be called when "Add" button is clicked.
     * @param event Event
     */
    @FXML
    public void handleAddAction(final ActionEvent event) {
        FXMLDialog addDialog = addController.getMyStage();
        if (null == addDialog) {
            addDialog = new FXMLDialog(addController, myStage, Modality.APPLICATION_MODAL);
        }
        addDialog.show();
    }

    /**
     * will be called when "Sub" button is clicked.
     * @param event Event
     */
    @FXML
    public void handleSubAction(final ActionEvent event) {
        FXMLDialog subDialog = subController.getMyStage();
        if (null == subDialog) {
            subDialog = new FXMLDialog(subController, myStage, Modality.APPLICATION_MODAL);
        }
        subDialog.show();
    }

    /**
     * will be called when "3D" button is clicked.
     * @param event Event
     */
    @FXML
    public void handle3DAction(final ActionEvent event) {
        // It's may be bug that the modeless child dialog stay upper layer of parent
        // in spite of click parent dialg and focus it.
        // So we must specify null as parent of modeless child dialog.
        // But that orphan dialog will not close even when top level dialog is closed.
        // Therefore we must close that orphan dialog manually, see onClose() method of this class.
        final FXMLDialog d3Dialog = new FXMLDialog(d3ExamController, null, Modality.NONE);
        d3Dialog.show();
    }

    /**
     * will be called when "Web" button is clicked.
     * @param event Event
     */
    @FXML
    public void handleWebViewAction(final ActionEvent event) {
        final FXMLDialog webExamDialog = new FXMLDialog(webExamController, myStage);
        webExamDialog.show();
    }

    /**
     * initialize this controller.
     * @param url URL
     * @param bundle Bundle
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        // observer add child controller.
        addController.addObserver(this);
        // observer sub child controller.
        subController.addObserver(this);
    }

    /**
     * will be called when child contrller notify update.
     * @param obj Controller
     * @param arg Message
     */
    @Override
    public void update(final Observable obj, final Object arg) {
        if (obj instanceof AddController) {
            txtSum.setText(String.format("Value=%d", entity.getVal()));
        } else if (obj instanceof SubController) {
            txtSum.setText(String.format("Value=%d", entity.getVal()));
        }
    }
    
    /**
     * call when dialog will be closed.
     * Close d3ExamDialog that is modeless.
     */
    @Override
    public void onClose() {
        Stage d3ExamDialog = d3ExamController.getMyStage();
        if (null != d3ExamDialog) {
            d3ExamDialog.close();
        }
    }
}
