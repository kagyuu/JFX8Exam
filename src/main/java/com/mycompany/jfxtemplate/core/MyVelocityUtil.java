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
 * Utility methods for Apache Velocity.
 *
 * @author atsushi
 *
 */
public final class MyVelocityUtil {

    static {
        final Logger logger = LoggerFactory.getLogger(MyVelocityUtil.class);
        final Properties prop = new Properties();
        try {
            final ClassLoader loader
                    = Thread.currentThread().getContextClassLoader();
            prop.load(loader.getResourceAsStream("velocity.properties"));
        } catch (IOException e) {
            logger.error("Faild to load velocity.properties.", e);
        }
        Velocity.init(prop);
    }

    /**
     * Disable default constructor.
     */
    private MyVelocityUtil() {
        super();
    }

    /**
     * merge template and key-value.
     *
     * @param template name of template file (classpath relative path)
     * @param keyVals variables
     * @return merged text
     */
    public static String merge(
            final String template, final Map<Object, Object> keyVals) {
        final Logger logger = LoggerFactory.getLogger("HTML");
        final VelocityContext ctx = new VelocityContext();
        keyVals.entrySet().stream().forEach((entry) -> {
            ctx.put(entry.getKey().toString(), entry.getValue());
        });

        Template t = Velocity.getTemplate(template);
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);

        if (logger.isDebugEnabled()) {
            logger.debug(sw.toString());
        }

        return sw.toString();
    }
}
