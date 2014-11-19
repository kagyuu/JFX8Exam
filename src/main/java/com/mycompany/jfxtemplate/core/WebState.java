/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

/**
 * State of WebView.
 *
 * @author atsushi
 */
public enum WebState {

    /**
     * loading.
     */
    LOADING,
    /**
     * success (I can use javascript functions from javafx).
     */
    ACCESSIBLE,
    /**
     * failed.
     */
    EXIT_FAIL
}
