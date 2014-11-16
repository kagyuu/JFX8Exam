/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.ui;

import com.mycompany.jfxtemplate.core.FXMLDialog;
import com.mycompany.jfxtemplate.d3.D3ExamController;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * Springの画面コントローラオブジェクト設定クラス.
 *
 * @author hondou
 */
@Configuration
@Lazy
public class UiConfiguration {

    /**
     * Primary Stage.
     */
    private Stage primaryStage;

    /**
     * set primary stage.
     *
     * @param primaryStage primary stage.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * @return Main Dialog.
     */
    @Bean
    @Scope("prototype")
    public FXMLDialog mainDialog() {
        return new FXMLDialog(mainController(), primaryStage);
    }

    /**
     * @return Main Controller.
     */
    @Bean
    @Scope("prototype")
    public MainController mainController() {
        return new MainController();
    }
    
    /**
     * @return Add Controller.
     */
    @Bean
    @Scope("prototype")
    public AddController addController() {
        return new AddController();
    }
    
    /**
     * @return Sub Controller.
     */
    @Bean
    @Scope("prototype")
    public SubController subController() {
        return new SubController();
    }
    
    /**
     * @return Sub Controller.
     */
    @Bean
    @Scope("prototype")
    public D3ExamController d3ExamController() {
        return new D3ExamController();
    }
}
