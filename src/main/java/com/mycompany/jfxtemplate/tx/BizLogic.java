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

    /**
     * add.
     * @param num number
     */
    public void add(final int num) {
        entity.setVal(entity.getVal() + num);
    }

    /**
     * sub.
     * @param num number
     */
    public void sub(final int num) {
        entity.setVal(entity.getVal() - num);
    }  
}
