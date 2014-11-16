/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.core;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;

/**
 * 画面コントローラクラスの Interface.
 * @author hondou
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

