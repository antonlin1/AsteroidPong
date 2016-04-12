package com.mygdx.game.Controller;

/**
 * Created by antonlin on 16-04-12.
 */
public interface AcceleratorInterface {

    public double[] getAccelerationRateOfChange();

    public double[] getAcceleration();

    public double getLastTime();

    public double[] getVelocity();

    public double[] getPosition();

}
