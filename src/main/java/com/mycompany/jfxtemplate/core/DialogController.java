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
   * @return FXML ファイル名
   */
  String fxml();

  /**
   * @return Window タイトル
   */
  String title();

  /**
   * @return アイコン
   */
  Image getIcon();
}

