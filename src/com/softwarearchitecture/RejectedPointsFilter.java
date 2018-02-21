package com.softwarearchitecture;

public class RejectedPointsFilter extends PressureFilterTemplate {

    private double pressure, time;

    public void run() {
        System.out.print("\n" + this.getName() + "::RejectedPointsFilter Reading");

        while (true) {
            try {
                readNextPairValues();

                if (id == Utils.TIME_ID) {
                    time = Double.longBitsToDouble(measurement);
                } else if (id == Utils.PRESSURE_ID) {
                    pressure = Double.longBitsToDouble(measurement);

                    if (isWildPoint(pressure)) {
                        writePairValuesToStream(Utils.TIME_ID, Double.doubleToLongBits(time));
                        writePairValuesToStream(Utils.PRESSURE_ID, Double.doubleToLongBits(pressure));
                    }
                }
            } catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + this.getName() + "::RejectedPointsFilter Exiting; bytes read: " + bytesRead
                        + " bytes written: " + bytesWritten);
                break;
            }
        }
    }
}
