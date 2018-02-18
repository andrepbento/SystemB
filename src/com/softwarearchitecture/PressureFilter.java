package com.softwarearchitecture;

abstract public class PressureFilter extends FilterTemplate {

    private static final int MIN_PRESSURE = 50;
    private static final int MAX_PRESSURE = 80;

    public boolean isWildSpot(double pressure) {
        return MIN_PRESSURE > pressure || MAX_PRESSURE < pressure;
    }

    abstract public void run();
} // PressureFilter
