/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mountain Bean.
 * for examination of calling javascript from java and calling java
 * from javascript.
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
