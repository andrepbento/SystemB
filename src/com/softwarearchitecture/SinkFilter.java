package com.softwarearchitecture;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SinkFilter extends FilterTemplate {

    private Calendar timeStamp = Calendar.getInstance();
    private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");

    private String fileName = null;
    private String stringFormat = "%-20s %-20s %-20s %-20s %n";

    private double temperature, altitude, pressure = 0;

    private boolean canWrite = false;

    public SinkFilter(String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        try {
            File file = new File(fileName);

            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(String.format(stringFormat, "Time:", "Temperature(C):", "Altitude(m):", "Pressure(psi):"));
            printWriter.flush();

            System.out.print("\n" + this.getName() + "::Sink Reading");

            while (true) {
                try {
                    readNextPairValues();

                    if (id == Utils.TIME_ID) {
                        timeStamp.setTimeInMillis(measurement);
                    } else if (id == Utils.ALTITUDE_ID) {
                        altitude = Double.longBitsToDouble(measurement);
                    } else if (id == Utils.PRESSURE_ID) {
                        pressure = Double.longBitsToDouble(measurement);
                    } else if (id == Utils.TEMPERATURE_ID) {
                        temperature = Double.longBitsToDouble(measurement);
                        canWrite = true;
                    }

                    if (canWrite) {
                        System.out.println("\n" + this.getName() + "::Sink Writing" + "\n");
                        printWriter.write(String.format(stringFormat,
                                timeStampFormat.format(timeStamp.getTime()),
                                temperature, altitude, pressure));
                        printWriter.flush();
                        canWrite = false;
                    }

                } catch (EndOfStreamException e) {
                    ClosePorts();
                    System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesRead
                            + " bytes written: " + bytesWritten);
                    break;
                }
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
