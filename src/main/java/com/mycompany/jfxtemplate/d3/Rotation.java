/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.d3;

/**
 * rotate t rad around (0,0,0)->(a,b,c).
 *
 * @author atsushi
 */
public class Rotation {

    public double t;
    public double a;
    public double b;
    public double c;

    public Rotation(double t, double a, double b, double c) {
        double len = Math.sqrt(a * a + b * b + c * c);

        this.t = t;
        this.a = a / len;
        this.b = b / len;
        this.c = c / len;
    }
    
    public Rotation(Quaternion q) {
        // TODO -t is correct answer too ... I don't know which is right.
        t = Math.acos(q.w) * 2.0;
        a = q.x / Math.sin(t/2.0);
        b = q.y / Math.sin(t/2.0);  // may be JIT compiler do well.
        c = q.z / Math.sin(t/2.0);  // may be JIT compiler do well.
    }
    
    @Override
    public String toString() {
        return String.format("(%03f, %03f, %03f, %03f)", 180.0 * t / Math.PI, a, b, c);
    }
}
 