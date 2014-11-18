/* All Rights Reserved, Copyright (C) com.mycompany
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
