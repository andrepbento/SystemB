package com.softwarearchitecture;

import java.util.HashMap;
import java.util.Set;

public class Frame {

    private HashMap<Integer, Long> frameData = new HashMap<>();

    public Frame() {
    }

    public Long getDataMeasurement(int id) {
        Long measurement = frameData.get(id);
        return measurement == null ? 0 : measurement;
    }

    public void setDataMeasurement(int id, Long measurement) {
        this.frameData.put(id, measurement);
    }

    public Long getTime() {
        return getDataMeasurement(Utils.TIME_ID);
    }

    public Long getAltitude() {
        return getDataMeasurement(Utils.ALTITUDE_ID);
    }

    public Long getPressure() {
        return getDataMeasurement(Utils.PRESSURE_ID);
    }

    public Long getTemperature() {
        return getDataMeasurement(Utils.TEMPERATURE_ID);
    }

    public Set<Integer> getFrameKeys() {
        return frameData.keySet();
    }
}
