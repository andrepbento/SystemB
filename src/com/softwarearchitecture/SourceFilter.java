package com.softwarearchitecture;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class SourceFilter extends FilterTemplate {
    public void run() {
        String fileName = "FlightData.dat";
        int bytesRead = 0;
        int bytesWritten = 0;
        DataInputStream in = null;

        byte dataByte = 0;

        try {
            in = new DataInputStream(new FileInputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source reading file...");

            while (true) {
                dataByte = in.readByte();
                bytesRead++;
                WriteFilterOutputPort(dataByte);
                bytesWritten++;
            } // while
        } // try
        catch (EOFException eofException) {
            System.out.println("\n" + this.getName() + "::End of file reached...");
            try {
                in.close();
                ClosePorts();
                System.out.println("\n" + this.getName() + "::Read file complete, bytes read::" + bytesRead + " bytes written: " + bytesWritten);

            } catch (Exception closeerr) {
                System.out.println("\n" + this.getName() + "::Problem closing input data file::" + closeerr);
            } // catch
        } catch (IOException iox) {
            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox);
        } // catch
    } // run
} // SourceFilter
