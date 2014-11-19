/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapper.
 * Jackson FAQ: Thread-Safety
 * http://wiki.fasterxml.com/JacksonFAQThreadSafety
 * The basic rule of Jackson thread-safety is that factories follow
 * "thread-safe after configuration" philosophy.
 *
 * @author atsushi
 */
public final class Jackson {

    /**
     * Singleton Object Mapper.
     */
    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * Disable default constructor.
     */
    private Jackson() {
        super();
    }
}
