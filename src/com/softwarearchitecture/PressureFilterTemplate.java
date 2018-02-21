package com.softwarearchitecture;

abstract public class PressureFilterTemplate extends FilterTemplate {

    protected static final double MIN_PRESSURE = 50.0;
    protected static final double MAX_PRESSURE = 80.0;

    boolean isWildPoint(double pressure) {
        return pressure < MIN_PRESSURE || pressure > MAX_PRESSURE;
    }

    abstract public void run();
}
