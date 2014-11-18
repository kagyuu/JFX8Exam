/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebViewとのデータのやり取りに使うBean.
 * @author hondou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MountainBean {
    /**
     * Mountain.
     */
    private String mountain;
    /**
     * Altitude.
     */
    private int altitude;
}
