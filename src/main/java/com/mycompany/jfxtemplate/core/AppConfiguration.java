/* All Rights Reserved, Copyright (C) com.mycompany
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
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Root config for Spring.
 *
 * @author atsushi
 */
@Configuration
@Import({UiConfiguration.class, TxConfiguration.class, MdlConfiguration.class})
@PropertySource("classpath:app.properties")
public class AppConfiguration {

    /**
     * @return app.properties
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer
            propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * @return Icon image
     */
    @Bean(name = "icon")
    public Image icon() {
        return new Image(Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("icon.png"));
    }

    /**
     * @return workdir
     */
    @Bean
    @Scope("singleton")
    public WorkDir workdir() {
        return new WorkDir();
    }
}
