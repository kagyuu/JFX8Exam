/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.core;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapper. Jackson FAQ: Thread-Safety
 * http://wiki.fasterxml.com/JacksonFAQThreadSafety The basic rule of Jackson
 * thread-safety is that factories follow "thread-safe after configuration"
 * philosophy.
 *
 * @author hondou
 */
public class Jackson {

    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }
}
