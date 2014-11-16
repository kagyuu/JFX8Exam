/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.mdl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * SpringのModelオブジェクト設定クラス.
 * @author hondou
 */
@Configuration
@Lazy
public class MdlConfiguration {
    /**
     * @return Enity.
     */
    @Bean
    @Scope("singleton")
    public Entity entity() {
        return new Entity();
    }    
}
