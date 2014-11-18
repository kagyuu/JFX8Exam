/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import java.util.Observable;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;

/**
 * 定型的な処理を実装した DialogController.
 *
 * @author atsushi
 */
public abstract class SimpleDialogController
    extends Observable implements DialogController {

    /**
     * 設定ファイル.
     */
    @Autowired
    private Environment env;

    /**
     * アイコン.
     */
    @Autowired
    @Qualifier("icon")
    private Image icon;
    
    /**
     * FXML ファイル名を返します.
     *
     * @return クラス名から末尾の "Controller" を ".fxml" に変更した文字列
     */
    @Override
    public final String fxml() {
        final String fullPath = getClass().getName();
        final String clazzName = fullPath.replaceAll(".*[.]", "");
        return clazzName.replaceAll("Controller$", ".fxml");
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
