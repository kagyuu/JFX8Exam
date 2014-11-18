/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

/**
 * WebViewの状態を指し示すフラグ.
 *
 * @author atsushi
 */
public enum WebState {

    /**
     * ロード中.
     */
    LOADING,
    /**
     * 読み込み終了(Javascript呼び出し可能).
     */
    ACCESSIBLE,
    /**
     * 読み込み失敗.
     */
    EXIT_FAIL
}
