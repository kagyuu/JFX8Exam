/* All Rights Reserved, Copyright (C) com.mycompany
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
 * @author atsushi
 */
@Configuration
@Lazy
public class UiConfiguration {

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
     * @return 3D Exam Controller.
     */
    @Bean
    @Scope("prototype")
    public D3ExamController d3ExamController() {
        return new D3ExamController();
    }
    
    /**
     * @return WebView Exam Controller.
     */
    @Bean
    @Scope("prototype")
    public WebExamController webExamController() {
        return new WebExamController();
    }
}
