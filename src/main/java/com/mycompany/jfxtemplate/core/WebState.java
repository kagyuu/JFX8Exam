/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.core;

/**
 * WebViewの状態を指し示すフラグ.
 *
 * @author hondou
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
