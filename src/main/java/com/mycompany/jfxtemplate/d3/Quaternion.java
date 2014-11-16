/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.d3;

/**
 * Q=(w;x y z).
 *
 * @author hondou
 */
public class Quaternion {

    public double w;
    public double x;
    public double y;
    public double z;

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(Rotation r) {
        this(
            Math.cos(r.t / 2.0), 
            r.a * Math.sin(r.t / 2.0), 
            r.b * Math.sin(r.t / 2.0), 
            r.c * Math.sin(r.t / 2.0)
        );
    }
    
    public Quaternion multilple(Quaternion q) {
        return new Quaternion(
            w * q.w - x * q.x - y * q.y - z * q.z,
            w * q.x + x * q.w + y * q.z - z * q.y,
            w * q.y - x * q.z + y * q.w + z * q.x,
            w * q.z + x * q.y - y * q.x + z * q.w
        );
    }
}
 