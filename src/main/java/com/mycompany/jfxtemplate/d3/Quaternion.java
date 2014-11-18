/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.d3;

/**
 * Q=(w;x y z).
 *
 * @author atsushi
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
 