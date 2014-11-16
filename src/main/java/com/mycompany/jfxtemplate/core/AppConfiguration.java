/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.core;

import com.mycompany.jfxtemplate.mdl.MdlConfiguration;
import com.mycompany.jfxtemplate.tx.TxConfiguration;
import com.mycompany.jfxtemplate.ui.UiConfiguration;
import javafx.scene.image.Image;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Springのルート設定クラス.
 *
 * @author hondou
 */
@Configuration
@Import({UiConfiguration.class, TxConfiguration.class, MdlConfiguration.class })
@PropertySource("classpath:app.properties")
public class AppConfiguration {

    /**
     * app.properties の内容を読み取ります.
     *
     * @return app.properties
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer
            propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * @return アイコンイメージ
     */
    @Bean(name = "icon")
    public Image icon() {
        return new Image(Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("icon.png"));
    }
}
