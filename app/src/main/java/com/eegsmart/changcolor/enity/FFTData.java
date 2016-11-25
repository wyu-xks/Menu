package com.eegsmart.changcolor.enity;

/**
 * Created by Xie on 2016/7/29.
 */
public class FFTData {

    public int noise;
//    public float delta ;
//    public float alpha;
    public double sleeping;
    public double el;
    public double eh;
    public int eyeMovement;
//    public int avg;
//    public int var;

//    public FFTData(int noise, float delta, float alpha, int var, int avg,float sleeping) {
//        this.noise = noise;
//        this.delta = delta;
//        this.alpha = alpha;
//        this.var = var;
//        this.avg = avg;
//        this.sleeping = sleeping;
//    }


    public FFTData(int noise, double sleeping, double el, double eh, int eyeMovement) {
        this.noise = noise;
        this.sleeping = sleeping;
        this.el = el;
        this.eh = eh;
        this.eyeMovement = eyeMovement;
    }
}
