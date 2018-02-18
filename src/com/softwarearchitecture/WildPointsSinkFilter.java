package com.softwarearchitecture;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WildPointsSinkFilter extends PressureFilter {

    private File file = new File("WildPoints.dat");
    private Calendar timeStamp = Calendar.getInstance();
    private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");

    String stringFormat = "%-20s %-20s%n";

    private int nFrame = 0;
    private double pressure;

    public void run() {
        try {
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(String.format(stringFormat, "Time:", "Pressure(psi):"));
            printWriter.flush();

            System.out.print("\n" + this.getName() + "::WildPointsSinkFilter Reading");

            while (true) {
                try {
                    readNextId();
                    readNextMeasurement();

                    if (id == Utils.TIME_ID) {
                        timeStamp.setTimeInMillis(measurement);
                        nFrame++;
                    } else if (id == Utils.PRESSURE_ID) {
                        pressure = Double.longBitsToDouble(measurement);
                    }

                    if (id == Utils.TIME_ID && nFrame > 1 && isWildSpot(pressure)) {
                        System.out.println("\n" + this.getName() + "::WildPointsSinkFilter Writing" + "\n");
                        printWriter.write(String.format(stringFormat,
                                timeStampFormat.format(timeStamp.getTime()), pressure));
                        printWriter.flush();
                    }

                    writeIdToStream();
                    writeMeasurementToStream();
                } // try
                catch (EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::WildPointsSinkFilter Exiting; bytes read: " + bytesRead
                            + " bytes written: " + bytesWritten);
                    break;
                } // catch
            } // while
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // run
} // WildPointsSinkFilter