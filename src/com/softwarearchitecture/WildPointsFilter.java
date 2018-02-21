package com.softwarearchitecture;

import java.util.ArrayList;

public class WildPointsFilter extends PressureFilterTemplate {

    private ArrayList<Frame> frameDataArray = new ArrayList<>();
    private Frame frame = new Frame();
    private double lastValidValue, nextValidValue = 0.0;

    private boolean isFirstTime, hasFixFirstFrame, hasFixPreviousFrames = false;

    private int nFrames = 1;

    @Override
    public void run() {

        while (true) {
            /*
            // Fix how to detect PipeStream END before trying to read data
                if (nFrames == 100) {
                    frame.setDataMeasurement(Utils.PRESSURE_ID, Double.doubleToLongBits(-1 * lastValidValue));
                    writeFramesToOutputStream();
                }
            */

            try {
                readNextPairValues();
            } catch (EndOfStreamException e) {
                e.printStackTrace();
                break;
            }

            if (id == Utils.TEMPERATURE_ID) {
                nFrames++;
                frame.setDataMeasurement(id, measurement);
                if (!isFirstTime) {
                    frameDataArray.add(frame);
                }
                isFirstTime = false;
                if (lastValidValue != 0 && nextValidValue != 0.0) {
                    if (!hasFixPreviousFrames) {
                        fixPreviousFrames();
                        hasFixPreviousFrames = true;
                        writeFramesToOutputStream();
                    }
                    writeFramesToOutputStream();
                }
                frame = new Frame();
            } else if (id == Utils.PRESSURE_ID) {
                double value = Double.longBitsToDouble(measurement);
                if (!isWildPoint(value)) {
                    if (lastValidValue != 0 && nextValidValue == 0.0) {
                        nextValidValue = value;
                    } else if (nextValidValue != 0.0) {
                        lastValidValue = nextValidValue;
                        nextValidValue = value;
                    } else if (lastValidValue == 0.0) {
                        lastValidValue = value;
                        if (!hasFixFirstFrame) {
                            Frame firstFrame = frameDataArray.get(0);
                            if (firstFrame != null)
                                firstFrame.setDataMeasurement(id, Double.doubleToLongBits(lastValidValue));
                            hasFixFirstFrame = true;
                        }
                    }
                    frame.setDataMeasurement(id, measurement);
                } else {
                    frame.setDataMeasurement(id, Double.doubleToLongBits(-1 * (lastValidValue + nextValidValue) / 2));
                }
            } else {
                frame.setDataMeasurement(id, measurement);
            }
        }

    }

    private void fixPreviousFrames() {
        Frame frame = frameDataArray.get(0);
        if (frame != null) {
            frame.setDataMeasurement(Utils.PRESSURE_ID, Double.doubleToLongBits(-1 * lastValidValue));
            for (int i = 1; i < frameDataArray.size() - 2; i++) {
                frame = frameDataArray.get(i);
                frame.setDataMeasurement(Utils.PRESSURE_ID, Double.doubleToLongBits(-1 * (lastValidValue + nextValidValue) / 2));
            }
        }
    }

    private void writeFramesToOutputStream() {
        for (Frame frame : frameDataArray)
            for (Integer key : frame.getFrameKeys())
                writePairValuesToStream(key, frame.getDataMeasurement(key));
        frameDataArray.clear();
    }
}
