/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Velocity で、文書を作成するためのユーティリティ部品.
 *
 * @author atsushi
 *
 */
public class MyVelocityUtil {

    static {
        Properties p = new Properties();
        try {
            p.load(MyVelocityUtil.class.getClassLoader().getResourceAsStream("velocity.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Velocity.init(p);
    }

    /**
     * コンストラクタ.
     */
    private MyVelocityUtil() {
        super();
    }

    /**
     * template に対して keyVals で穴埋めした結果を返します.
     *
     * @param template テンプレートファイル名 (Classpath 相対)
     * @param keyVals テンプレートに埋め込むオブジェクト
     * @return マージ結果
     */
    public static String merge(final String template, final Map<Object, Object> keyVals) {
        Logger logger = LoggerFactory.getLogger("HTML");
        VelocityContext ctx = new VelocityContext();
        for (Map.Entry<Object, Object> entry : keyVals.entrySet()) {
            ctx.put(entry.getKey().toString(), entry.getValue());
        }

        Template t = Velocity.getTemplate(template);
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);

        if (logger.isDebugEnabled()) {
            logger.debug(sw.toString());
        }

        return sw.toString();
    }
}
