/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;

/**
 * Dialog Controller Interface.
 * @author atsushi
 */
public interface DialogController extends Initializable {
  /**
   * @return FXML file name
   */
  String fxml();

  /**
   * @return Window title
   */
  String title();

  /**
   * @return Icon image
   */
  Image getIcon();
}

