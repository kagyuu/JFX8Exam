/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import java.util.Observable;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;

/**
 * Dialog Controller with commonly implementation.
 *
 * @author atsushi
 */
public abstract class AbstractDialogController
    extends Observable implements DialogController {

    /**
     * Settings (app.properties).
     */
    @Autowired
    private Environment env;

    /**
     * Icon.
     */
    @Autowired
    @Qualifier("icon")
    private Image icon;

    /**
     * FXML file name.
     * 
     * @return replace postfix "Controller" of the dialog-controller
     * class name to ".fxml"
     */
    @Override
    public final String fxml() {
        final String clazzName = getClass().getSimpleName();
        return "/fxml/" + clazzName.replaceAll("Controller$", ".fxml");
    }

    /**
     * Dialog のタイトルを返します.
     * 設定ファイルに、"title.コントローラクラス名=タイトル" 形式で定義されて
     * いる文字列を取得します。
     *
     * @return Window タイトル
     */
    @Override
    public final String title() {
        return env.getProperty("title." + getClass().getName());
    }

    /**
     * アイコンを返します.
     * UiConfig で読み込んだ icon.png です
     *
     * @return アイコン.
     */
    @Override
    public final Image getIcon() {
        return icon;
    }
}
