/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate;

import com.mycompany.jfxtemplate.core.AppConfiguration;
import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.ui.UiConfiguration;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * アプリケーション起動クラス.
 * @author hondou
 */
public final class MainApp extends Application {

    /**
     * JavaFXに制御が移った後の初期化処理.
     * @param stage Stage
     */
    @Override
    public void start(final Stage stage) {
        // Java FX 8 のスレッド上で Spring Container を立ち上げる
        final ApplicationContext context
            = new AnnotationConfigApplicationContext(AppConfiguration.class);

        // Spring から UIConfiguration を取得する
        final UiConfiguration uiConfig = context.getBean(UiConfiguration.class);
        uiConfig.setPrimaryStage(stage);

        // UIConfiguration から、初期画面を取得。
        // 初期画面が Spring によってインスタンス化されるので、初期画面以降のオブ
        // ジェクトは、すべて Spring によって DI される。
        // ( 初期画面で子画面が @Autowired されていたら、Spring によって、適切な
        // オブジェクトが DI されている。また、子画面内の @Autowired も DI されて
        // いる。孫画面の ... )
        final FXMLDialog mainStage = uiConfig.mainDialog();
        mainStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

}
