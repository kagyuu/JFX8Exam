/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.tx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * Springの業務ロジックオブジェクト設定クラス.
 * @author atsushi
 */
@Configuration
@Lazy
public class TxConfiguration {
    /**
     * @return Buziness Logic.
     */
    @Bean
    @Scope("prototype")
    public BizLogic bizLogic() {
        return new BizLogic();
    }
}
