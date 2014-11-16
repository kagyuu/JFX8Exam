/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring 管理下の画面コントローラと FXMLから作られる GUI を結びつけるための
 * Java FX の Stage の拡張.
 * @author hondou
 */
public class FXMLDialog extends Stage {
    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(FXMLDialog.class);

    /**
     * コンストラクタ. Window枠表示、モーダル指定です
     *
     * @param controller コントローラ
     * @param owner 親画面
     */
    public FXMLDialog(final DialogController controller, final Window owner) {
        this(controller, owner, StageStyle.DECORATED, Modality.WINDOW_MODAL);
    }

    /**
     * コンストラクタ. Window枠表示です
     *
     * @param controller コントローラ
     * @param owner 親画面
     * @param modal モーダル
     */
    public FXMLDialog(final DialogController controller, final Window owner,
            final Modality modal) {
        this(controller, owner, StageStyle.DECORATED, modal);
    }

    /**
     * コンストラクタ. モーダル指定です
     *
     * @param controller コントローラ
     * @param owner 親画面
     * @param style Window枠
     */
    public FXMLDialog(final DialogController controller, final Window owner,
            final StageStyle style) {
        this(controller, owner, style, Modality.WINDOW_MODAL);
    }

    /**
     * コンストラクタ.
     *
     * @param controller コントローラ
     * @param owner 親画面
     * @param style Window枠
     * @param modal モーダル
     */
    public FXMLDialog(final DialogController controller, final Window owner,
            final StageStyle style, final Modality modal) {
        super(style);

        LOGGER.trace("画面を作成します。fxml={}", controller.fxml());

        initOwner(owner);
        initModality(modal);
        final URL fxml = controller.getClass().getResource(controller.fxml());
        final FXMLLoader loader = new FXMLLoader(fxml);
        try {
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(final Class<?> aClass) {
                    return controller;
                }
            });
            setScene(new Scene((Parent) loader.load()));
            setTitle(controller.title());
            getIcons().add(controller.getIcon());

            wireThis(controller);
        } catch (IOException e) {
            LOGGER.error("画面作成に失敗しました", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Controller の @MyDialg アノテーションがついたフィールド変数に、この FXMLDialg のインスタンスを設定します.
     *
     * @param controller コントローラ
     */
    private void wireThis(final DialogController controller) {

        for (Field f : controller.getClass().getDeclaredFields()) {
            for (Annotation a : f.getAnnotations()) {
                if (a instanceof MyDialog) {
                    f.setAccessible(true);
                    try {
                        f.set(controller, this);
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        LOGGER.error("画面コントローラへの @MyDialog のInjectionに失敗しました", ex);
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }
}
