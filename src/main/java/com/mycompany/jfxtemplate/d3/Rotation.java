/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
 */
package com.mycompany.jfxtemplate.d3;

/**
 * (0,0,0)->(a,b,c) 周りに t rad 回転.
 *
 * @author hondou
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
        t = Math.acos(q.w) * 2.0;
        a = q.x / Math.sin(t/2.0);
        b = q.y / Math.sin(t/2.0);
        c = q.z / Math.sin(t/2.0);
    }
    
    @Override
    public String toString() {
        return String.format("(%03f, %03f, %03f, %03f)", 180.0 * t / Math.PI, a, b, c);
    }
}
 