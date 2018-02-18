package com.softwarearchitecture;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SinkFilter extends PressureFilter {

    private File file = new File("OutputB.dat");
    private Calendar timeStamp = Calendar.getInstance();
    private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");

    String stringFormat = "%-20s %-20s %-20s %-20s %n";

    private int nFrame = 0;
    private double temperature, altitude, pressure, lastValidValue;
    private double firstValidValue = 0;

    public void run() {
        try {
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(String.format(stringFormat, "Time:", "Temperature(C):", "Altitude(m):", "Pressure(psi):"));
            printWriter.flush();

            System.out.print("\n" + this.getName() + "::Sink Reading");

            while (true) {
                try {
                    readNextId();
                    readNextMeasurement();

                    if (id == Utils.TIME_ID) {
                        timeStamp.setTimeInMillis(measurement);
                        nFrame++;
                    } else if (id == Utils.TEMPERATURE_ID) {
                        temperature = Double.longBitsToDouble(measurement);
                    } else if (id == Utils.ALTITUDE_ID) {
                        altitude = Double.longBitsToDouble(measurement);
                    } else if (id == Utils.PRESSURE_ID) {
                        pressure = Double.longBitsToDouble(measurement);
                    }

                    if (id == Utils.TIME_ID && nFrame > 1) {
                        System.out.println("\n" + this.getName() + "::Sink Writing" + "\n");
                        if (isWildSpot(pressure)) {

                            if (lastValidValue < pressure) {

                            }
                            printWriter.write(String.format(stringFormat,
                                    timeStampFormat.format(timeStamp.getTime()), temperature, altitude, pressure + "*"));
                            printWriter.flush();
                        } else {
                            if (firstValidValue == 0) {
                                firstValidValue = pressure;
                                printWriter.write(String.format(stringFormat,
                                        timeStampFormat.format(timeStamp.getTime()), temperature, altitude, pressure));
                            }
                            printWriter.write(String.format(stringFormat,
                                    timeStampFormat.format(timeStamp.getTime()), temperature, altitude, pressure));
                            printWriter.flush();
                        }
                    }

                    writeIdToStream();
                    writeMeasurementToStream();
                } // try
                catch (EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesRead
                            + " bytes written: " + bytesWritten);
                    break;
                } // catch
            } // while
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // run
} // SinkFilter