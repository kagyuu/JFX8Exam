/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.mdl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * Model config for Spring.
 * @author atsushi
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
