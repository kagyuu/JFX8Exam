/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.core;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.scene.image.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;

/**
 * 定型的な処理を実装した DialogController.
 *
 * @author hondou
 */
public class SimpleDialogController
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
     * 初期化. なにもしません
     *
     * @param url URL
     * @param bundle Bundle
     *
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
    }

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
