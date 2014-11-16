/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.tx;

import com.mycompany.jfxtemplate.mdl.Entity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Business Logic.
 * @author atsushi
 */
public class BizLogic {
    
    /**
     * Entity.
     */
    @Autowired
    private Entity entity;
    
    public void add(int num) {
        entity.setVal(entity.getVal() + num);
    }
    
    public void sub(int num) {
        entity.setVal(entity.getVal() - num);
    }  
}
