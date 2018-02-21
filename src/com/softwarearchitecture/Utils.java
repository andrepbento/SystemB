package com.softwarearchitecture;

import java.nio.ByteBuffer;

public class Utils {

    public static final int ID_LENGTH = 4;
    public static final int MEASUREMENT_LENGTH = 8;

    public static final int TIME_ID = 0;
    public static final int ALTITUDE_ID = 2;
    public static final int PRESSURE_ID = 3;
    public static final int TEMPERATURE_ID = 4;
    public static final int PITCH_ID = 5;
    public static final int TOTAL_MEASUREMENT_COUNT = 5;

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[Integer.BYTES]);
        buffer.putInt(x);
        return buffer.array();
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[Long.BYTES]);
        buffer.putLong(x);
        return buffer.array();
    }
}
