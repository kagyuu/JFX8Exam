/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebViewとのデータのやり取りに使うBean.
 * @author atsushi
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
